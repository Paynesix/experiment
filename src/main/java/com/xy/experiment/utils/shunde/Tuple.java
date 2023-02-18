package com.xy.experiment.utils.shunde;

public class Tuple {
    public Tuple() {
    }

    public static class Tuple2<T1, T2> {
        private T1 t1;
        private T2 t2;

        public Tuple2(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public T1 _1() {
            return this.t1;
        }

        public T2 _2() {
            return this.t2;
        }
    }
}
