package com.goodtime.bruaket.extra.crafttweaker;

import crafttweaker.IAction;

public abstract class Action implements IAction {
    protected final String name;

    public Action(String name) {
        this.name = name;
    }
}
