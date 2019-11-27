package in.eoninfotech.eontechnician;


public class CardItem {

    private String mTextResource;
    private String mTitleResource;
    private String mImageUri;

    public CardItem(String smonth, String sname, String uri) {

        mTitleResource = smonth;
        mTextResource = sname;
        mImageUri = uri;
    }

    public String getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitleResource;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
