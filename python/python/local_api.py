import os

from fastapi import FastAPI, Form
from starlette.responses import PlainTextResponse

import python.live_audio as basik

import json

app = FastAPI()

# These variables MUST be part of the app otherwise other workers will
#   not be able to access them. Most variables will initially be set by a source outside basik,
#   then set again by basik, once basik is running.

# I/O
app.selected_input = -1
app.selected_output = -1
app.remove_pos = -1

# Effects
app.current_board = []
# Start Variables For Adding an Effect (VFANE)
app.desired_position_in_board = None
app.parameters = None
# End VFANE

# Fake CLI, it will be None after accessed once (via API)
app.cli_input = None


# START !only intended to be used by basik backend! (denoted as comment: !*!)
@app.get("/effect")
async def get_effect():
    global app
    if app.desired_position_in_board is None:
        return None
    temp_params_with_pos = app.parameters.split(",")
    temp_params_with_pos.append("POSITION:" + str(app.desired_position_in_board))
    app.desired_position_in_board = None
    app.parameters = None
    return temp_params_with_pos


@app.patch("/effect")
async def adjust_effect(desired_position_in_board: int = Form(...),
                        current_position_in_board: int = Form(...), parameters: str = Form(...)):
    global app
    app.desired_position_in_board = str(current_position_in_board) + "/" + str(desired_position_in_board)
    app.parameters = parameters
    # essentially tell the interface we want to adjust an effect
    app.cli_input = "a"
    return "Effect adjustment queued..."


@app.put("/effect")
async def add_effect(desired_position_in_board: int = Form(...),
                     effect_number: int = Form(...), parameters: str = Form(...)):
    global app
    app.desired_position_in_board = desired_position_in_board
    app.parameters = parameters
    # essentially tell the interface we want to add an effect
    app.cli_input = effect_number
    return "Effect addition queued..."


@app.delete("/effect")
async def remove_effect(effect_position_in_board: int = Form(...)):
    global app
    app.cli_input = "r"
    app.remove_pos = effect_position_in_board
    return "Effect removal queued..."


@app.get("/remove-pos", response_class=PlainTextResponse)
async def get_cli():
    global app
    # since we are faking cli input, this should only be accessible once
    temp_pos = app.remove_pos
    app.remove_pos = -1
    return json.dumps(temp_pos)


@app.get("/cli", response_class=PlainTextResponse)
async def get_cli():
    global app
    # since we are faking cli input, this should only be accessible once
    temp_cli = app.cli_input
    app.cli_input = None
    return json.dumps(temp_cli)


# LAST !*!
# LAST !*!
# LAST !*!
# LAST !*!
@app.put("/cli")
async def set_cli(command: str = Form(...)):
    global app
    print(command)
    app.cli_input = command
    return "Set input to \"" + app.cli_input + "\". This will be nullified after a single get request."


# get the current board
@app.get("/pedalboard")
async def get_current_board():
    global app
    return json.dumps(app.current_board)


# Rather than adding or removing from this local board variable,
#   I think it is better to just replace with the current running board from basik.
#   Otherwise, there could be some sort of mismatch.
@app.put("/pedalboard")
async def replace_current_board(new_board: str):
    global app
    app.current_board = new_board


@app.get("/outputs")
async def get_outputs():
    return json.dumps(basik.get_output_devices())


@app.get("/inputs")
async def get_inputs():
    return json.dumps(basik.get_input_devices())


@app.get("/input")
async def get_input():
    global app
    return json.dumps(app.selected_input)


@app.put("/input")
async def set_input(input_number: int = Form(...)):
    global app
    app.selected_input = input_number


@app.delete("/input")
async def set_input():
    global app
    app.selected_input = -1


@app.get("/output")
async def get_input():
    global app
    return json.dumps(app.selected_output)


@app.put("/output")
async def set_input(output_number: int = Form(...)):
    global app
    app.selected_output = output_number


@app.delete("/output")
async def set_input():
    global app
    app.selected_output = -1


@app.delete("/stop-api")
async def start_basik():
    os.system('kill %d' % os.getpid())
