package interfaces;

import entity.Hall;
import java.util.List;

public interface HallRepositoryInterface {

    boolean testConnection();

    void addHall(String name, int totalSeats);

    List<Hall> getAllHalls();

    Hall getHallById(int id);
}