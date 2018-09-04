package pro.kinect.taxi.db;

import net.orange_box.storebox.annotations.method.KeyByString;

public interface AppPrefs {

    @KeyByString("last_get_auto")
    long getLastTimeGettingAuto();
    @KeyByString("last_get_auto")
    void setLastTimeGettingAuto(long lastTimeGettingAuto);
}
