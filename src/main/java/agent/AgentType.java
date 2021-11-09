package agent;

public enum AgentType {
    B{
        public char getChar(){
            return 'B';
        }
    },
    V{
        public char getChar(){
            return 'V';
        }
    },
    E{
        public char getChar(){
            return 'E';
        }
    },
    R{
        public char getChar(){
            return 'R';
        }
    };

    abstract public char getChar();
}
