public class T1<T> {
    private  String a = "haha" ;

    public T1(String a)
    {
        this.a = a;
    }

    public <T> T test_fx(T t){

        System.out.println(t.getClass());
        System.out.println(t);

        return  t;
    }

    public static void main(String[] args) {
        T1<String> t1 = new T1("Hello T1 !");

        t1.test_fx(t1.a);
        t1.test_fx(312);


    }

}
