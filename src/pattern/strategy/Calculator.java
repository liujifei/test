package pattern.strategy;


public enum Calculator {
    
    ADD(){
        public int exec(int a, int b){
            return a+b;
        }
    },
    SUB(){
        public int exec(int a, int b){
            return a-b;
        }
    },
    MULTY(){
        public int exec(int a, int b){
            return a*b;
        }
    };
    private Calculator(){
    }

    public abstract int exec(int a, int b);
}
