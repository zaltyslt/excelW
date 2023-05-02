package dataobjects;

public class ShortCommand {
    int start, end, length;
    String name;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ShortCommand{" +
                "name='" + name + '\'' +
                "start=" + start +
                ", end=" + end +
                ", length=" + length +

                '}';
    }
}
