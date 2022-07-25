package com.flightleader.engine.actions;

import com.flightleader.engine.State;

public interface Action {

	public State performAction(State state, Object message);
}
