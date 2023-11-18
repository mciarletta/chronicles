package learn.chronicles.data;

import learn.chronicles.models.Figure;

import java.util.List;

public interface FigureRepository {

    Figure findById(int id);

    List<Figure> findByBoardGameId(int id);

    Figure create(Figure figure);

    boolean update(Figure figure);

    boolean deleteById(int id);
}
