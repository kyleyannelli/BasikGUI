import time

import pedalboard
from pedalboard import Chorus, Reverb, Distortion, Clipping, Gain, Compressor, HighpassFilter, LowpassFilter, Convolution
from pedalboard.io import AudioStream

import sounddevice as sd

import os

import requests
from pedalboard_native.utils import Chain
from requests.adapters import HTTPAdapter
s = requests.Session()
s.mount("http://", HTTPAdapter(max_retries=999))
url = "http://localhost:30108"
input_url = url + "/input"
output_url = url + "/output"
cli_url = url + "/cli"

distortion_amp = [Compressor(), Gain(gain_db=20), Clipping(threshold_db=-10), HighpassFilter(cutoff_frequency_hz=250),
                  Gain(gain_db=-15), Distortion(drive_db=15.1), Gain(gain_db=20),
                  HighpassFilter(cutoff_frequency_hz=250), Gain(gain_db=-15), Distortion(drive_db=15.1),
                  Gain(gain_db=20), HighpassFilter(cutoff_frequency_hz=250), Gain(gain_db=-15),
                  Distortion(drive_db=15.1), Gain(gain_db=-10), Gain(gain_db=20), LowpassFilter(cutoff_frequency_hz=6000),
                  Gain(gain_db=-15)]
distortion_ir = Convolution("/Users/kyle/IdeaProjects/Seminar/python/impulse-responses/distortion/OD-R112-V30-DYN-57-P09-00.wav")

clean_amp = [Compressor(), Gain(gain_db=-20), Clipping(threshold_db=-10), HighpassFilter(cutoff_frequency_hz=250),
             Gain(gain_db=-15), Distortion(drive_db=15.1), Gain(35)]
clean_ir = Convolution("/Users/kyle/IdeaProjects/Seminar/python/impulse-responses/clean/01_Twin73_dome_edge_L19.wav")

chain_size: int = 0
amp_start_pos: int = -1
amp_end_pos: int = -1
ir_pos: int = -1


def start():
    global input_url
    selected_input_device: int = sync_get_data(input_url, -1)

    global output_url
    selected_output_device: int = sync_get_data(output_url, -1)

    global cli_url
    with AudioStream(buffer_size=1200, input_device_name=str(set_input_device(selected_input_device)),
                     output_device_name=str(set_output_device(selected_output_device)), allow_feedback=True) as stream:
        command_in = ""
        set_clean_amp(stream.plugins)
        is_dist = False
        while is_user_continue(command_in):
            is_dist = handle_input(stream=stream, user_input=command_in, is_dist=is_dist)
            command_in = sync_get_data(cli_url, "null")


def sync_get_data(get_url: str, continue_case):
    current_case = continue_case
    while str(continue_case) == str(current_case):
        time.sleep(0.2)
        current_case = s.get(get_url).text.replace("\"", "")
        current_case.replace("\\", "")
    return current_case


def start_cli():
    list_input_devices()

    selected_input_device = input("\nPlease select an input device (by number): ")

    list_output_devices()

    selected_output_device = input("\nPlease select an output device (by number): ")

    with AudioStream(buffer_size=1200, input_device_name=str(set_input_device(selected_input_device)),
                     output_device_name=str(set_output_device(selected_output_device)), allow_feedback=True) as stream:
        # stream.running.
        user_in = ""
        # start on clean channel
        set_clean_amp(stream.plugins)
        is_dist = False
        while is_user_continue(user_in):
            is_dist = handle_input(stream=stream, user_input=user_in, is_dist=is_dist)
            # memory overflow if input is spammed too quickly
            time.sleep(0.01)
            # make sure we aren't windows as clear isn't a windows command
            if os.name != 'nt':
                os.system("clear")
            print_board(stream, is_dist)
            user_in = input("\n1 for Reverb, 2 for Distortion, 3 for Chorus. 4 to toggle Clean/Distortion Channel\n"
                            + "Enter a to adjust an effect, r to remove an effect.\n" +
                            "Enter q to quit.\n")


def remove_amp(pedal_chain: Chain):
    global amp_start_pos, amp_end_pos, ir_pos, chain_size
    # keep track of total original
    i = 0
    # keep track of current position (since chain is changing size)
    j = 0
    # remove the ir before the loop as we won't know its position after
    if ir_pos >= 0:
        pedal_chain.remove(pedal_chain.__getitem__(ir_pos))
    ir_pos = -1
    chain_size -= 1
    for pedal in pedal_chain:
        if i == ir_pos:
            pedal_chain.remove(pedal_chain.__getitem__(j))
            chain_size -= 1
            j -= 1
        if amp_start_pos <= i <= amp_end_pos:
            pedal_chain.remove(pedal_chain.__getitem__(j))
            chain_size -= 1
            j -= 1
        if i > amp_end_pos:
            break
        i += 1
        j += 1
    amp_start_pos = -1
    amp_end_pos = -1


def set_clean_amp(pedal_chain: Chain):
    global clean_amp, clean_ir
    set_amp(pedal_chain, clean_amp, clean_ir)


def set_distortion_amp(pedal_chain: Chain):
    global distortion_amp, distortion_ir
    set_amp(pedal_chain, distortion_amp, distortion_ir)


def set_amp(pedal_chain: Chain, amp_chain: list, amp_ir: Convolution):
    global amp_start_pos, amp_end_pos, ir_pos, \
        chain_size
    remove_amp(pedal_chain)
    amp_start_pos = chain_size
    for pedal in amp_chain:
        pedal_chain.append(pedal)
        chain_size += 1
    amp_end_pos = chain_size
    pedal_chain.append(amp_ir)
    chain_size += 1
    ir_pos = chain_size


def print_board(stream: AudioStream, dist: bool):
    if dist:
        print("\n***Current Board | DISTORTION CHANNEL***")
    else:
        print("\n***Current Board | CLEAN CHANNEL***")
    i = 0
    for plugin in stream.plugins:
        if not(amp_start_pos <= i <= amp_end_pos) and i != ir_pos:
            print("[" + str(i) + "]", end=" ")
            print_plugin(plugin)
        i += 1
    print("*******************")


def is_recognized_plugin(plugin: pedalboard.Plugin):
    if type(plugin) == pedalboard.Reverb:
        return True
    elif type(plugin) == pedalboard.Chorus:
        return True
    elif type(plugin) == pedalboard.Distortion and \
            not (15.11 > plugin.drive_db > 15.09):
        return True
    return False


def print_plugin(plugin: pedalboard.Plugin):
    if type(plugin) == pedalboard.Reverb:
        print("|Reverb|: "
              + "Width: " + str(plugin.width)
              + ", Damping: " + str(plugin.damping)
              + ", Room Size: " + str(plugin.room_size)
              + ", Dry Level: " + str(plugin.dry_level))
    elif type(plugin) == pedalboard.Chorus:
        print("|Chorus|: "
              + "Mix: " + str(plugin.mix)
              + ", Depth: " + str(plugin.depth)
              + ", Feedback: " + str(plugin.feedback)
              + ", Rate (hz): " + str(plugin.rate_hz)
              + ", Centre Delay (ms): " + str(plugin.centre_delay_ms))
    elif type(plugin) == pedalboard.Distortion:
        if not (15.11 > plugin.drive_db > 15.09):
            print("|Distortion|: "
                  + "Drive (db): " + str(plugin.drive_db))


def handle_input(stream: AudioStream, user_input: str, is_dist: bool):
    if user_input.isdigit():
        if int(user_input) == 4:
            if is_dist:
                set_clean_amp(stream.plugins)
                return False
            else:
                set_distortion_amp(stream.plugins)
                return True
        add_effect(stream, user_input)
    elif user_input == "r":
        remove_effect(stream)
    elif user_input == "a":
        adjust_effect(stream)
    return is_dist


def adjust_effect(stream: AudioStream):
    requested_adjust = input("Adjust? (effect number): ")
    i = 0
    if requested_adjust.isdigit():
        requested_adjust = int(requested_adjust)
        for plugin in stream.plugins:
            if i == requested_adjust:
                adjust_plugin(plugin)
                return
            i += 1


def adjust_plugin(plugin: pedalboard.Plugin):
    print("Press enter/return to leave any values unchanged.")
    if type(plugin) == pedalboard.Reverb:
        width = input("Width? (" + str(plugin.width) + "): ")
        damping = input("Damping? (" + str(plugin.damping) + "): ")
        room_size = input("Room Size? (" + str(plugin.room_size) + "): ")
        dry_level = input("Dry Level? (" + str(plugin.dry_level) + "): ")
        if width.count(".") < 2 and width.replace(".", "").isnumeric():
            plugin.width = float(width)
        if damping.count(".") < 2 and damping.replace(".", "").isnumeric():
            plugin.damping = float(damping)
        if room_size.count(".") < 2 and room_size.replace(".", "").isnumeric():
            plugin.room_size = float(room_size)
        if dry_level.count(".") < 2 and dry_level.replace(".", "").isnumeric():
            plugin.dry_level = float(dry_level)
    elif type(plugin) == pedalboard.Chorus:
        mix = input("Mix? (" + str(plugin.mix) + "): ")
        depth = input("Depth? (" + str(plugin.depth) + "): ")
        feedback = input("Feedback? (" + str(plugin.feedback) + "): ")
        rate_hz = input("Rate (Hz)? (" + str(plugin.rate_hz) + "): ")
        centre_delay_ms = input("Centre Delay (ms)? (" + str(plugin.centre_delay_ms) + "): ")
        if mix.count(".") < 2 and mix.replace(".", "").isnumeric():
            plugin.mix = float(mix)
        if depth.count(".") < 2 and depth.replace(".", "").isnumeric():
            plugin.depth = float(depth)
        if feedback.count(".") < 2 and feedback.replace(".", "").isnumeric():
            plugin.feedback = float(feedback)
        if rate_hz.count(".") < 2 and rate_hz.replace(".", "").isnumeric():
            plugin.rate_hz = float(rate_hz)
        if centre_delay_ms.count(".") < 2 and centre_delay_ms.replace(".", "").isnumeric():
            plugin.centre_delay_ms = float(centre_delay_ms)
    elif type(plugin) == pedalboard.Distortion:
        drive_db = input("Drive (db)? (" + str(plugin.drive_db) + "): ")
        if drive_db.count(".") < 2 and drive_db.replace(".", "").isnumeric():
            plugin.drive_db = float(drive_db)


def add_pedal(pedal_chain: Chain, pedal: pedalboard.Plugin, pre_amp: bool):
    global amp_start_pos, amp_end_pos, ir_pos, chain_size
    if pre_amp:
        pedal_chain.insert(amp_start_pos + 1, pedal)
        # the entire amp is shifting over
        amp_start_pos += 1
        amp_end_pos += 1
        ir_pos += 1
    else:
        # just the speaker is shifting over
        pedal_chain.insert(ir_pos, pedal)
        ir_pos += 1
    chain_size += 1


def add_effect(stream: AudioStream, user_input: str):
    added_effect = None
    # add reverb
    if int(user_input) == 1:
        added_effect = Reverb()
    elif int(user_input) == 2:
        added_effect = Distortion()
    elif int(user_input) == 3:
        added_effect = Chorus()
    elif int(user_input) == 4:
        added_effect = None
    if added_effect is not None:
        add_pedal(stream.plugins, added_effect, False)


def remove_effect(stream: AudioStream):
    requested_remove = input("Remove? (effect number): ")
    i = 0
    if requested_remove.isdigit():
        requested_remove = int(requested_remove)
        for plugin in stream.plugins:
            if i == requested_remove:
                stream.plugins.remove(plugin)
                return
            i += 1


def is_user_continue(user_input):
    if str(user_input) == "q":
        return False
    return True


def list_output_devices():
    # list output devices
    print()
    for device in get_output_devices():
        print(device)


def list_input_devices():
    # list input devices
    print()
    for device in get_input_devices():
        print(device)


def get_output_devices():
    output_devices_array = []
    i = 0
    for device in query_devices_refresh():
        if device["max_output_channels"] > 0:
            output_devices_array.append("[" + str(i) + "] " + str(device["name"]))
            i += 1
    return output_devices_array


def get_input_devices():
    input_devices_array = []
    i = 0
    for device in query_devices_refresh():
        if device["max_input_channels"] > 0:
            input_devices_array.append("[" + str(i) + "] " + str(device["name"]))
            i += 1
    return input_devices_array


# taken from https://github.com/spatialaudio/python-sounddevice/issues/47#issuecomment-258461697
def query_devices_refresh(device=None):
    sd._terminate()
    sd._initialize()
    return sd.query_devices(device)


def set_input_device(device_number):
    i = 0
    for device in query_devices_refresh():
        if device["max_input_channels"] > 0:
            if str(i) == str(device_number):
                selected_input = device["name"]
                return selected_input
            i += 1
    return 0


def set_output_device(device_number):
    i = 0
    for device in query_devices_refresh():
        if device["max_output_channels"] > 0:
            if str(i) == str(device_number):
                selected_output = device["name"]
                return selected_output
            i += 1
    return 0


# start_cli()
start()
