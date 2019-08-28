package com.spectradrive.Helpers;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
 import android.text.style.URLSpan;

import com.spectradrive.SpectraDrive;
import com.spectradrive.android.R;

public class URLSpanNoUnderline extends URLSpan {
      public URLSpanNoUnderline(String p_Url) {  
           super(p_Url);  
      }  
      public void updateDrawState(TextPaint p_DrawState) {
          super.updateDrawState(p_DrawState);
           p_DrawState.setUnderlineText(false);
      }
 }