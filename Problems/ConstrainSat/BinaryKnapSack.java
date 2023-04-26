package Problems.ConstrainSat;


import General.IState;
import General.Operator;
import General.Problem;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * The problem is to maximise the value of the bag we are picking
 * subject to the weight capacity
 * TODO: this module is not done at all, need to refactor and finish in the future, im ahead of my time writing this
 *
 */
public class BinaryKnapSack {
    public float[] values;
    public float[] weights;
    public float Capacity;

    public BinaryKnapSackProblem problem;
    private static class BinaryKnapSackProblem extends Problem
    {

        public BinaryKnapSackProblem(IState init, IState end, Operator[] operators) {
            super(init, end, operators);
        }

    }
    static class BagState implements IState
    {
        BagState Parent;
        private Set<Integer> bag;
        private int Capacity;
        private float[] values;
        private float[] weights;
        BagState(int Capacity, float[] values, float[] weights, Set<Integer> bag)
        {
            this.Capacity = Capacity;
            this.values = values;
            this.weights = weights;
            this.bag = bag;
        }

        public int getCapacity() {
            return Capacity;
        }

        public float[] getValues() {
            return values;
        }

        public float[] getWeights() {
            return weights;
        }

        public Set<Integer> getBag() {
            return bag;
        }
        public float getSumValues()
        {
            float sum =0;
            for(Integer index : bag)
            {
                sum += values[index];
            }
            return sum;
        }
        public float getSumWeights()
        {
            float sum =0;
            for(Integer index : bag)
            {
                sum += weights[index];
            }
            return sum;
        }
        @Override
        public boolean equals(IState state2) {
            return ((BagState)state2).bag.equals(bag);
        }
        @Override
        public LinkedList<IState> GetPath() {
            LinkedList<IState> path = new LinkedList<>();
            BagState cur = this;
            path.add(this);
            while(!cur.Parent.equals(cur))
            {
                cur = cur.Parent;
                path.add(Parent);
            }
            return path;
        }

        @Override
        public double getCost() {
            return 0;
        }

        @Override
        public void setCost(double cost) {

        }

        @Override
        public void SetParent(IState state) {
            Parent = (BagState) state;
        }

        @Override
        public IState GetParent() {
            return null;
        }

        public String toString()
        {
            Object[] sorted_arr = this.bag.stream().sorted().toArray();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < sorted_arr.length; i++) {
                builder.append(String.format(" %d ", (Integer)sorted_arr[i]));
            }
            return builder.toString();
        }
    }
    static class KnapOperator implements Operator
    {
        int itemIndex;
        boolean isPick;
        public KnapOperator(int itemIndex, boolean isPick)
        {
            this.itemIndex = itemIndex;
            this.isPick = isPick;
        }
        @Override
        public IState operate(IState s)
        // pick up the item to the bag
        {
            assert CheckPossible(s);
            BagState state = (BagState) s;
            HashSet<Integer> set = new HashSet<Integer>(state.getBag());
            set.add(itemIndex);
            return new BagState(state.Capacity, state.values, state.weights, set);
        }

        @Override
        public boolean CheckPossible(IState s) {
            BagState state = (BagState) s;
            if(isPick && !state.getBag().contains(itemIndex))
            {
                return state.getSumWeights() + state.weights[itemIndex] <= state.Capacity;
            }
            else if (!isPick && state.getBag().contains(itemIndex))
            // we can always remove if we have the object in the bag
            {
                return true;
            }
            // illigal any other case
            return false;
        }

    }
    private Operator[] getOperators()
    {
        Operator[] operators = new Operator[this.weights.length * 2];
        for (int i = 0; i < operators.length; i++) {
            if(i % 2 == 0)
            {
                operators[i] = new KnapOperator(i, true);
            }
            else
            {
                operators[i] = new KnapOperator(i, false);
            }
        }
        return operators;
    }
    public BinaryKnapSack(float[] values, float[] weights, float Capacity)
    {
        assert values.length == weights.length;
        assert Capacity > 0;
        this.values = values;
        this.weights = weights;
        this.Capacity = Capacity;

        // create operators
        Operator[] operators = getOperators();
//        this.problem = new BinaryKnapSackProblem();

    }



}
