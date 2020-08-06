public class TimeInterval {
    double startTime, endTime;

    public TimeInterval() {
    }

    public double getEndTime() {
        return endTime;
    }


    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getDurationOfTimeInterval() {
        return endTime - startTime;
    }

    public boolean isInTimeInterval(double time) {
        if (time >= getStartTime() && time <= getEndTime()) return true;
        return false;
    }

    public String toString() {
        return "(" + getStartTime() + ", " + getEndTime() + ")";
    }

}
