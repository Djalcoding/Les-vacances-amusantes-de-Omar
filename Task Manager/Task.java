import java.util.List;
import java.util.ArrayList;;

public class Task {
    
    private int position;
    private Power power;
    private List<Runnable> tasks = new ArrayList<>();

    public Task(int position, int voltage, int current){
        this.position = position;
        this.power = new Power(voltage, current);
    }

    public void pushTask(Runnable task){
        tasks.add(task);
    }

    public void next(){
        if(!tasks.isEmpty()){
            tasks.remove(0).run();
        }
    }

    public void runAll(){
        while(!tasks.isEmpty()){
            next();
        }
    }
    
    public static void main(String[] args)  {
        if( args.length > 3){
            System.err.println("Too many arguments");
            return;
        }
        if( args.length < 3){
            System.err.println("Not enough arguments");
            return;
        }
        
        int position, voltage, current;

        try {
            position = Integer.parseInt(args[0]);
            voltage = Integer.parseInt(args[1]);
            current = Integer.parseInt(args[2]);

        } catch (NumberFormatException e) {
            System.err.println("One argument isn't a number");
            return;
        }
        
        Task manager = new Task(position, voltage, current);

        manager.pushTask(() -> {
        if (manager.position == 0) {
            System.out.println("At floor 0 — moving up by 1");
            manager.position += 1;
        }
    });

    manager.pushTask(() -> {
        if (manager.power.voltage >= 3) {
            System.out.println("High voltage — moving up by 2");
            manager.position += 2;
        }
    });

    manager.pushTask(() -> {
        if (manager.power.current > 4) {
            System.out.println("High current detected — going down by 1");
            manager.position -= 1;
        }
    });

    manager.pushTask(() -> {
        if (manager.power.voltage > 4 && manager.power.current > 4) {
            System.out.println("System overloaded — emergency drop by 3");
            manager.position -= 3;
        }
    });

    manager.pushTask(() -> {
        if (manager.power.voltage <= 0) {
            System.out.println("Battery dead — returning to ground floor");
            manager.position = 0;
        }
    });

    manager.pushTask(() -> {
        if (manager.power.current <= 1) {
            System.out.println("Not enough power to stay — going down by 2");
            manager.position -= 2;
        }
    });

    manager.pushTask(() -> {
        if (manager.power.current <= 0 && manager.power.voltage <= 0) {
            System.out.println("Total failure — going to floor 0");
            manager.position = 0;
        }
    });

        manager.pushTask(() -> {
            if (manager.power.voltage < 1) {
                System.out.println("Battery low! Going to position down");
                manager.position = 1;
            }
        });

       manager.pushTask(() -> {
            if (manager.position < 1) {
                System.out.println("How did you get to the basement. Going back up to 1");
                manager.position = 1;
            }
        }); 

        manager.pushTask(() -> {
            if (manager.position > 10) {
                System.out.println("You are too high. Hope u fall. Back to floor 1");
                manager.position = 1;
            }
        });
        
        manager.runAll();
        System.out.println(manager.position);
        System.out.println(manager.power.voltage);
        System.out.println(manager.power.current);
    }

    public class Power {
        private int voltage;
        private int current;
        
        public Power(int voltage, int current) {
            this.voltage = voltage;
            this.current = current;
        }
        
        public int getVoltage() {
            return voltage;
        }
        
        public void setVoltage(int voltage) {
            this.voltage = voltage;
        }
        
        public int getCurrent() {
            return current;
        }
        
        public void setCurrent(int current) {
            this.current = current;
        }
    }
}
