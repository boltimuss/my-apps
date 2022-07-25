package com.flightleader.engine.rules;

import com.flightleader.engine.State;

public interface Rule {

	public boolean isValid(State state, Object message);
}
