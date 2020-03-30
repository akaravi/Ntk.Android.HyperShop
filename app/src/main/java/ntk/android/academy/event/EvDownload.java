package ntk.android.academy.event;

public class EvDownload {

    private boolean DataChange;

    public EvDownload(boolean DC) {
        this.DataChange = DC;
    }

    public boolean DataChange() {
        return DataChange;
    }
}
