package pro.kinect.taxi;

import android.app.Application;
import android.arch.persistence.room.Room;

import pro.kinect.taxi.db.AppDatabase;

public class App extends Application {

    private static App instance;
    private AppDatabase database;

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String db = "kinect_taxi.db";
        database = Room.databaseBuilder(this, AppDatabase.class, db).build();
    }

    public static App getInstance() {
        return instance;
    }

    public static AppDatabase getDatabase() {
        return getInstance().database;
    }
}
