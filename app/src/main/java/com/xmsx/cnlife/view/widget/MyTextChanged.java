package com.xmsx.cnlife.view.widget;

import android.text.Editable;
import android.text.TextWatcher;


/**
 *  没实现onTextChanged，减少页面重复写beforeTextChanged，afterTextChanged
 */

public abstract class MyTextChanged implements TextWatcher{

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
