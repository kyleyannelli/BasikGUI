import time

import pedalboard
from pedalboard import Chorus, Reverb, Distortion, Clipping, Gain, Compressor, HighpassFilter, LowpassFilter, Convolution
from pedalboard.io import AudioStream

import sounddevice as sd

import os

import requests
from requests.adapters import HTTPAdapter
s = requests.Session()
s.mount("http://", HTTPAdapter(max_retries=999))
url = "http://localhost:30108"
input_url = url + "/input"
output_url = url + "/output"


def start():
    global input_url
    selected_input_device: int = sync_get_data(input_url, -1)

    global output_url
    selected_output_device: int = sync_get_data(output_url, -1)

    with AudioStream(buffer_size=1200, input_device_name=str(set_input_device(selected_input_device)),
                     output_device_name=str(set_output_device(selected_output_device)), allow_feedback=True) as stream:
        input("hey")


def sync_get_data(get_url: str, continue_case):
    current_case = continue_case
    while str(continue_case) == str(current_case):
        time.sleep(0.2)
        current_case = s.get(get_url).text.replace("\"", "")
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


def print_board(stream: AudioStream, dist: bool):
    if dist:
        print("\n***Current Board | DISTORTION CHANNEL***")
    else:
        print("\n***Current Board | CLEAN CHANNEL***")
    i = 0
    for plugin in stream.plugins:
        if is_recognized_plugin(plugin):
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
        add_effect(stream, user_input)
        if int(user_input) == 4:
            if is_dist:
                return False
            else:
                return True
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
        # start here
        stream.plugins.append(Compressor())
        stream.plugins.append(Gain(gain_db=20))
        stream.plugins.append(Clipping(threshold_db=-10))
        stream.plugins.append(HighpassFilter(cutoff_frequency_hz=250))
        stream.plugins.append(Gain(gain_db=-15))
        stream.plugins.append(Distortion(drive_db=15.1))
        # end here ^ for just crunch
        stream.plugins.append(Gain(gain_db=20))
        stream.plugins.append(HighpassFilter(cutoff_frequency_hz=250))
        stream.plugins.append(Gain(gain_db=-15))
        stream.plugins.append(Distortion(drive_db=15.1))
        stream.plugins.append(Gain(gain_db=20))
        stream.plugins.append(HighpassFilter(cutoff_frequency_hz=250))
        stream.plugins.append(Gain(gain_db=-15))
        stream.plugins.append(Distortion(drive_db=15.1))
        stream.plugins.append(Gain(gain_db=-10))
        # extra gain stage
        stream.plugins.append(Gain(gain_db=20))
        stream.plugins.append(LowpassFilter(cutoff_frequency_hz=6000))
        stream.plugins.append(Gain(gain_db=-15))
        stream.plugins.append(Convolution("impulse-responses/distortion/OD-R112-V30-DYN-57-P09-00.wav"))
    if added_effect is not None:
        stream.plugins.append(added_effect)


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


start_cli()
# start()
