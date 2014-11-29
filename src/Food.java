/**
 * Created by Maria on 26.11.2014.
 */
public class Food {
    public int x;
    public int y;
    public long value = 1;
    public boolean isGood = true;

    Food(){}
    Food(int x, int y, long value, boolean isGood) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.isGood = isGood;
    }
}
