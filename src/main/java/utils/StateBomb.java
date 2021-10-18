package utils;

public enum StateBomb {
    Step0, Step1, Step2, Step3, Boom;

    private static StateBomb[] vals = values();
    public StateBomb next(){
        return vals[(this.ordinal()+1) % vals.length];
    }
}


