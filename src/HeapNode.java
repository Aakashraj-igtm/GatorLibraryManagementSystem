import java.util.Date;

public class HeapNode implements Comparable<HeapNode> {
    public int patronId;
    public int priorityNumber;
    public Date timeOfReservation;

    public HeapNode(int patronId, int priorityNumber, Date timeOfReservation) {
        this.patronId = patronId;
        this.priorityNumber = priorityNumber;
        this.timeOfReservation = timeOfReservation;
    }

    // Overriding compareTo method , comparing by priorityNumber first and breaking ties with timeOfReservation
    @Override
    public int compareTo(HeapNode other){
        if (this.priorityNumber != other.priorityNumber)
            return this.priorityNumber - other.priorityNumber;
        else
            return this.timeOfReservation.compareTo(other.timeOfReservation);
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public void setPriorityNumber(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public Date getTimeOfReservation() {
        return timeOfReservation;
    }

    public void setTimeOfReservation(Date timeOfReservation) {
        this.timeOfReservation = timeOfReservation;
    }
}