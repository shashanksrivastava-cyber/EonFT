package in.eoninfotech.eontechnician.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import in.eoninfotech.eontechnician.R;
/***************************************************************************/
// Copyright EON Infotech Ltd., unpublished work, created 2016.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/

/**
 * Created by root on 18/11/16.
 */
public class MyTextView extends TextView {

    public MyTextView(Context context) {
        super(context);
        this.setTextColor(Color.parseColor("#4789da"));
        this.setTextSize(15);
        this.setPadding(5,3,3,3);

    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextColor(Color.parseColor("#4789da"));
        this.setTextSize(15);
        this.setPadding(5,3,3,3);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTextColor(Color.parseColor("#4789da"));
        this.setTextSize(15);
        this.setPadding(5,3,3,3);
    }
}
