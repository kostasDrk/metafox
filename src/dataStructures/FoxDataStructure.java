/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import symbols.value.Value;

public abstract class FoxDataStructure {

    public abstract void put(Value key, Value value);
    public abstract Value get(Value key);
    public abstract void remove(Value key);
    public abstract int size();
    public abstract HashMap<Value, Value> values();
}
