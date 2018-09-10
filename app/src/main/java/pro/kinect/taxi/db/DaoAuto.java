package pro.kinect.taxi.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DaoAuto {

    @Query("SELECT * FROM EntityAuto")
    List<EntityAuto> getAllAuto();

    @Query("SELECT * FROM EntityAuto")
    Flowable<EntityAuto> getFlowableAllAuto();

    @Query("SELECT * FROM EntityAuto" +
            " where statenumber like '%' || :request || '%'"
            + " or drivename like '%' || :request || '%'"
            + " or driverphone like '%' || :request || '%'"
    )
    List<EntityAuto> searchAuto(String request);

    @Query("SELECT * from EntityAuto where id = :id LIMIT 1")
    Flowable<EntityAuto> getAutoById(int id);

    @Query("SELECT * from EntityAuto where statenumber = :stateNumber LIMIT 1")
    EntityAuto getAutoByNumber(String stateNumber);

    @Insert
    long insert(EntityAuto auto);

    @Insert
    long[] insert(List<EntityAuto> autoList);

    @Update
    void update(EntityAuto auto);

    @Update
    void update(List<EntityAuto> autoList);

    @Delete
    void delete(EntityAuto auto);

    @Delete
    void delete(List<EntityAuto> autoList);
}
