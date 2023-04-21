package General;

import java.util.LinkedList;

/**
 * Example State implementation, you should create a state for yourself per problem
 */
public class State implements IState{

    State Parent;
    Object Value;


    public State()
    {
        this.Parent = this;
    }

    public Object getValue() {
        return Value;
    }

    @Override
    public boolean equals(IState state2) {
        return this.Value.equals(((State)state2).getValue());
    }

    @Override
    public LinkedList<IState> GetPath() {
        return null;
    }

    @Override
    public void SetParent(IState state) {
        this.Parent = (State)state;
    }

    @Override
    public IState GetParent()
    {
        return this.Parent;
    }


}
