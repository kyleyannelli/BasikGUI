import time

import pedalboard
from pedalboard import Chorus, Reverb, Distortion, \
    Clipping, Gain, Compressor, HighpassFilter, Convolution, Delay
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
effect_url = url + "/effect"
pedalboard_url = url + "/pedalboard"
remove_pos_url = url + "/remove-pos"

distortion_amp = [Compressor(), Gain(gain_db=20), Clipping(threshold_db=-10), HighpassFilter(cutoff_frequency_hz=250),
                  Gain(gain_db=-15), Distortion(drive_db=15.1), Gain(gain_db=20), Gain(gain_db=-15), Distortion(drive_db=15.1),
                  Gain(gain_db=20), Gain(gain_db=-15),
                  Distortion(drive_db=15.1), Gain(gain_db=-10), Gain(gain_db=20), Gain(gain_db=-15)]
distortion_ir = Convolution(os.path.dirname(__file__) +
                            "/../impulse-responses/distortion/OD-R112-V30-DYN-57-P09-00.wav")

clean_amp = [Compressor(), Gain(gain_db=-20),
             HighpassFilter(cutoff_frequency_hz=250), Gain(gain_db=-15), Distortion(drive_db=15.1), Gain(35)]
clean_ir = Convolution(os.path.dirname(__file__) + "/../impulse-responses/distortion/OD-R112-V30-DYN-57-P10-30-BRIGHT.wav")

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
    with AudioStream(buffer_size=1024, input_device_name=str(set_input_device(selected_input_device)),
                     output_device_name=str(set_output_device(selected_output_device)), allow_feedback=True) as stream:
        handle_api_stream(stream)


def handle_api_stream(stream: AudioStream):
    command_in = ""
    set_clean_amp(stream.plugins)
    is_dist = False
    while is_user_continue(command_in):
        is_dist = handle_input_through_api(stream=stream, user_input=command_in, is_dist=is_dist)
        update_api_pedalboard(stream.plugins)
        command_in = sync_get_data(cli_url, "null")


def is_position_legal(position: int):
    return (position >= 0) and ((position <= amp_start_pos) or (amp_end_pos <= position <= ir_pos))


def update_api_pedalboard(pedal_chain: Chain):
    stringified_board = ""
    i = 0
    for pedal in pedal_chain:
        pedal: pedalboard.Plugin = pedal
        if (i < amp_start_pos) or (amp_end_pos < i < ir_pos):
            stringified_board += "|" + stringify_pedal(pedal, i)
        i += 1
    sync_put_data(pedalboard_url, {'new_board': stringified_board})


def stringify_pedal(plugin: pedalboard.Plugin, position: int):
    if type(plugin) == pedalboard.Reverb:
        return "name:reverb,position:" + str(position) + ",damping:" + str(plugin.damping) + \
            ",room_size:" + str(plugin.room_size) + \
            ",dry_level:" + str(plugin.dry_level) + \
            ",wet_level:" + str(plugin.wet_level)
    elif type(plugin) == pedalboard.Chorus:
        return "name:chorus,position:" + str(position) + ",mix:" + str(plugin.mix) + \
            ",depth:" + str(plugin.depth) + \
            ",rate_hz:" + str(plugin.rate_hz)
    elif type(plugin) == pedalboard.Distortion:
        return "name:distortion,position:" + str(position) + ",drive_db:" + str(plugin.drive_db)
    elif type(plugin) == pedalboard.Delay:
        return "name:delay,position:" + str(position) + ",mix:" + str(plugin.mix) + \
            ",feedback:" + str(plugin.feedback) + \
            ",delay_seconds:" + str(plugin.delay_seconds)
    else:
        return str(plugin)


def sync_get_data(get_url: str, continue_case):
    current_case = continue_case
    while str(continue_case) == str(current_case):
        time.sleep(0.2)
        current_case = s.get(get_url).text.replace("\"", "")
        current_case.replace("\\", "")
    return current_case


def sync_put_data(put_url: str, data: dict):
    s.put(url=put_url, params=data)


def remove_amp(pedal_chain: Chain):
    global amp_start_pos, amp_end_pos, ir_pos, chain_size
    for x in range(chain_size - 1, -1, -1):
        pedal_chain.remove(pedal_chain.__getitem__(x))
    chain_size = 0
    amp_start_pos = -1
    amp_end_pos = -1
    ir_pos = -1
    # # remove the ir before the loop as we won't know its position after
    # if ir_pos >= 0:
    #     pedal_chain.remove(pedal_chain.__getitem__(ir_pos))
    #     ir_pos = -1
    #     chain_size -= 1
    # if amp_start_pos != -1:
    #     x = amp_end_pos
    #     while x >= amp_start_pos:
    #         pedal_chain.remove(pedal_chain.__getitem__(x))
    #         chain_size -= 1
    #         x -= 1
    #     amp_start_pos = -1
    #     amp_end_pos = -1


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
    if chain_size > 0:
        amp_start_pos = chain_size - 1
    else:
        amp_start_pos = 0
    for pedal in amp_chain:
        pedal_chain.append(pedal)
        chain_size += 1
    amp_end_pos = chain_size - 1
    pedal_chain.append(amp_ir)
    chain_size += 1
    ir_pos = chain_size - 1


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


def add_pedal_through_api(pedal_chain: Chain, effect_number: int, pre_amp: bool):
    global amp_start_pos, amp_end_pos, ir_pos, chain_size, effect_url
    effect_info_from_api: dict = get_dict_effect_from_api()
    preferred_pos = int(effect_info_from_api["POSITION"])
    pedal: pedalboard.Plugin = parse_effect_from_dict(effect_number, effect_info_from_api)
    if pre_amp and (amp_start_pos >= preferred_pos >= 0):
        pedal_chain.insert(preferred_pos, pedal)
        # the entire amp is shifting over
        amp_start_pos += 1
        amp_end_pos += 1
        ir_pos += 1
    elif pre_amp:
        pedal_chain.insert(amp_start_pos, pedal)
        # the entire amp is shifting over
        amp_start_pos += 1
        amp_end_pos += 1
        ir_pos += 1
    else:
        # just the speaker is shifting over
        pedal_chain.insert(ir_pos, pedal)
        ir_pos += 1
    chain_size += 1


def parse_effect_from_dict(effect_number: int, parameters: dict, pedal: pedalboard.Plugin = None):
    added_effect = None
    if effect_number == 1:
        added_effect: pedalboard.Reverb = Reverb()
        adjust_reverb(added_effect, parameters, pedal)
    elif effect_number == 2:
        added_effect: pedalboard.Distortion = Distortion()
        adjust_distortion(added_effect, parameters, pedal)
    elif effect_number == 3:
        added_effect: pedalboard.Chorus = Chorus()
        adjust_chorus(added_effect, parameters, pedal)
    elif effect_number == 5:
        added_effect: pedalboard.Delay = Delay()
        adjust_delay(added_effect, parameters, pedal)
    if added_effect is not None:
        return added_effect


def adjust_delay(delay_pedal: pedalboard.Delay, parameters: dict, pedal: pedalboard.Delay = None):
    if "mix" in parameters:
        delay_pedal.mix = float(parameters["mix"])
        if pedal is not None:
            delay_pedal.feedback = pedal.feedback
            delay_pedal.delay_seconds = pedal.delay_seconds
    if "feedback" in parameters:
        delay_pedal.feedback = float(parameters["feedback"])
        if pedal is not None:
            delay_pedal.mix = pedal.mix
            delay_pedal.delay_seconds = pedal.delay_seconds
    if "delay_seconds" in parameters:
        delay_pedal.delay_seconds = float(parameters["delay_seconds"])
        if pedal is not None:
            delay_pedal.feedback = pedal.feedback
            delay_pedal.mix = pedal.mix


def adjust_chorus(chorus_pedal: pedalboard.Chorus, parameters: dict, pedal: pedalboard.Chorus = None):
    if "mix" in parameters:
        chorus_pedal.mix = float(parameters["mix"])
        if pedal is not None:
            chorus_pedal.feedback = pedal.feedback
            chorus_pedal.depth = pedal.depth
            chorus_pedal.rate_hz = pedal.rate_hz
    if "depth" in parameters:
        chorus_pedal.depth = float(parameters["depth"])
        if pedal is not None:
            chorus_pedal.feedback = pedal.feedback
            chorus_pedal.mix = pedal.mix
            chorus_pedal.rate_hz = pedal.rate_hz
    if "rate_hz" in parameters:
        rate_hz_normalized: float = float(parameters["rate_hz"])
        if rate_hz_normalized < 0.8:
            chorus_pedal.rate_hz = (rate_hz_normalized * 4)
        elif rate_hz_normalized < 0.85:
            chorus_pedal.rate_hz = rate_hz_normalized * 20
        elif rate_hz_normalized < 0.9:
            chorus_pedal.rate_hz = rate_hz_normalized * 40
        elif rate_hz_normalized < 0.95:
            chorus_pedal.rate_hz = rate_hz_normalized * 60
        else:
            chorus_pedal.rate_hz = rate_hz_normalized * 100
        if pedal is not None:
            chorus_pedal.feedback = pedal.feedback
            chorus_pedal.depth = pedal.depth
            chorus_pedal.mix = pedal.mix


def adjust_distortion(distortion_pedal: pedalboard.Distortion, parameters: dict, pedal: pedalboard.Distortion = None):
    if "drive_db" in parameters:
        distortion_pedal.drive_db = float(parameters["drive_db"])


def adjust_reverb(reverb_pedal: pedalboard.Reverb, parameters: dict, pedal: pedalboard.Reverb = None):
    if "mix" in parameters:
        reverb_pedal.wet_level = float(parameters["mix"])
        reverb_pedal.dry_level = 1.0 - reverb_pedal.wet_level
        if pedal is not None:
            reverb_pedal.damping = pedal.damping
            reverb_pedal.room_size = pedal.room_size
    if "damping" in parameters:
        reverb_pedal.damping = float(parameters["damping"])
        if pedal is not None:
            reverb_pedal.wet_level = pedal.wet_level
            reverb_pedal.dry_level = pedal.dry_level
            reverb_pedal.room_size = pedal.room_size
    if "room_size" in parameters:
        reverb_pedal.room_size = float(parameters["room_size"])
        if pedal is not None:
            reverb_pedal.wet_level = pedal.wet_level
            reverb_pedal.dry_level = pedal.dry_level
            reverb_pedal.damping = pedal.damping


def get_dict_effect_from_api():
    list_data: list = sync_get_data(effect_url, "null").replace("[", "").replace("]", "").split(",")
    dict_data: dict = {}
    for data in list_data:
        data_split = data.split(":")
        dict_data[data_split[0]] = data_split[1]
    return dict_data


def handle_input_through_api(stream: AudioStream, user_input: str, is_dist: bool):
    if user_input.isdigit():
        if int(user_input) == 4:
            if is_dist:
                set_clean_amp(stream.plugins)
                return False
            else:
                set_distortion_amp(stream.plugins)
                return True
        add_pedal_through_api(stream.plugins, int(user_input), True)
    elif user_input == "r":
        remove_effect_through_api(stream.plugins)
    elif user_input == "a":
        adjust_effect_through_api(stream.plugins)
    return is_dist


def remove_effect_through_api(pedal_chain: Chain):
    global amp_start_pos, amp_end_pos, ir_pos, chain_size
    position: int = int(sync_get_data(remove_pos_url, -1))
    if not(is_position_legal(position)):
        return
    pedal_chain.remove(pedal_chain.__getitem__(position))
    if is_pre(position):
        amp_start_pos -= 1
        amp_end_pos -= 1
        chain_size -= 1
        ir_pos -= 1
    else:
        ir_pos -= 1
        chain_size -= 1


def adjust_effect_through_api(pedal_chain: Chain):
    global amp_start_pos, amp_end_pos
    effect_info_from_api: dict = get_dict_effect_from_api()
    positions: list = effect_info_from_api["POSITION"].split("/")
    current_pos = int(positions[0])
    preferred_pos = int(positions[1])
    # make sure the position is an available position. not in the middle of the amp and so on.
    if current_pos >= chain_size or \
            preferred_pos >= chain_size or \
            not(is_position_legal(preferred_pos)):
        return

    pedal: pedalboard.Plugin = pedal_chain.__getitem__(current_pos)
    adjusted_pedal = None
    if type(pedal) == pedalboard.Reverb:
        if "KEEP" in effect_info_from_api:
            adjusted_pedal = parse_effect_from_dict(1, effect_info_from_api, pedal)
        else:
            adjusted_pedal = parse_effect_from_dict(1, effect_info_from_api)
    elif type(pedal) == pedalboard.Chorus:
        if "KEEP" in effect_info_from_api:
            adjusted_pedal = parse_effect_from_dict(3, effect_info_from_api, pedal)
        else:
            adjusted_pedal = parse_effect_from_dict(3, effect_info_from_api)
    elif type(pedal) == pedalboard.Distortion:
        if "KEEP" in effect_info_from_api:
            adjusted_pedal = parse_effect_from_dict(2, effect_info_from_api, pedal)
        else:
            adjusted_pedal = parse_effect_from_dict(2, effect_info_from_api)
    elif type(pedal) == pedalboard.Delay:
        if "KEEP" in effect_info_from_api:
            adjusted_pedal = parse_effect_from_dict(5, effect_info_from_api, pedal)
        else:
            adjusted_pedal = parse_effect_from_dict(5, effect_info_from_api)
    # if there wasn't a match for pedal type then stop
    if adjusted_pedal is None:
        return

    if preferred_pos <= current_pos:
        pedal_chain.insert(preferred_pos, adjusted_pedal)
        pedal_chain.remove(pedal_chain.__getitem__(current_pos + 1))
    else:
        pedal_chain.insert(preferred_pos + 1, adjusted_pedal)
        pedal_chain.remove(pedal_chain.__getitem__(current_pos))

    # if the original position was pre-amp, and now it is post, move amp positions back one step
    if is_pre(current_pos) and not(is_pre(preferred_pos)):
        if amp_start_pos > 0:
            amp_start_pos -= 1
            amp_end_pos -= 1
    # if the original position was post-amp, and now it is pre, move amp positions up one step
    elif not(is_pre(current_pos)) and is_pre(preferred_pos):
        amp_start_pos += 1
        amp_end_pos += 1
    # otherwise the positions of the amp are unaffected. IR should always be last, so untouched.


def is_pre(position: int):
    return position <= amp_start_pos


def add_pedal(pedal_chain: Chain, pedal: pedalboard.Plugin, pre_amp: bool):
    global amp_start_pos, amp_end_pos, ir_pos, chain_size
    if pre_amp:
        pedal_chain.insert(amp_start_pos, pedal)
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


# start()
