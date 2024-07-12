package com.example.llc.android_r.text;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * com.example.llc.android_r.text.DynamicMarqueeTextView
 *
 * @author liulongchao
 * @since 2024/6/14
 */
public class DynamicMarqueeTextView extends AppCompatTextView {

    private static final String ELLIPSIS = "…";
    private Handler handler;
    private StringBuilder fullTextBuilder;
    private String previousText = "";
    private int index;
    private int maxLinesToShow = 2;
    private boolean isRunning = false;

    public DynamicMarqueeTextView(Context context) {
        super(context);
        init();
    }

    public DynamicMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        handler = new Handler();
        fullTextBuilder = new StringBuilder();
    }

    public void setTextWithDynamicMarquee(final String newText, final long delay) {
        // Find the different part between previousText and newText
        String differentPart = getDifferentPart(previousText, newText);
        previousText = newText;

        if (!differentPart.isEmpty()) {
            fullTextBuilder.append(differentPart);
            index = 0;

            // If the marquee is already running, stop it before starting a new one.
            if (isRunning) {
                handler.removeCallbacksAndMessages(null);
            }

            isRunning = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index <= fullTextBuilder.length()) {
                        String displayedText = getDisplayedText();
                        setText(displayedText);
                        index++;
                        handler.postDelayed(this, delay);
                    } else {
                        isRunning = false; // Stop the marquee when done.
                    }
                }
            }, delay);
        }
    }

    private String getDifferentPart(String oldText, String newText) {
        int minLength = Math.min(oldText.length(), newText.length());
        int diffIndex = minLength;

        for (int i = 0; i < minLength; i++) {
            if (oldText.charAt(i) != newText.charAt(i)) {
                diffIndex = i;
                break;
            }
        }

        return newText.substring(diffIndex);
    }

    private String getDisplayedText() {
        String tempText = fullTextBuilder.substring(0, index);
        Paint paint = getPaint();
        int lineCount = 0;
        String[] lines = tempText.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            int start = 0;
            int end;
            while (start < line.length()) {
                end = paint.breakText(line, start, line.length(), true, getWidth(), null);
                result.append(line, start, start + end).append("\n");
                start += end;
                lineCount++;
                if (lineCount == maxLinesToShow) {
                    break;
                }
            }
            if (lineCount == maxLinesToShow) {
                break;
            }
        }

        if (lineCount <= maxLinesToShow) {
            return result.append(fullTextBuilder.substring(index)).toString().trim();
        } else {
            String truncatedText = result.toString().trim();
            int lastSpace = truncatedText.lastIndexOf(' ');
            if (lastSpace != -1) {
                truncatedText = truncatedText.substring(0, lastSpace);
            }
            truncatedText += ELLIPSIS;

            int ellipsisPosition = truncatedText.length();
            int visibleCharCount = truncatedText.length() + fullTextBuilder.length() - index;
            int hiddenCharCount = fullTextBuilder.length() - index;

            if (hiddenCharCount > 0) {
                if (visibleCharCount > 0) {
                    return truncatedText + fullTextBuilder.substring(index - visibleCharCount, index);
                } else {
                    return truncatedText;
                }
            } else {
                return truncatedText + fullTextBuilder.substring(index);
            }
        }
    }
}

