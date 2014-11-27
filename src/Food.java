/**
 * Created by Maria on 26.11.2014.
 */
public class Food {
    public int x;
    public int y;
    public int value;

    Food(int x, int y, int power) {
        this.x = x;
        this.y = y;
        this.value = (int)(Math.pow(2, power));
    }
}
