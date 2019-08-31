package zhangchongantest.neu.edu.newtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class CustomPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private ImageView imageView;
    private ImageViewClickLisnter lisnter;
    public CustomPopupWindow(Context context) {
        this(context,null);
    }

    public CustomPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childView = inflater.inflate(R.layout.popup_view,null);
        imageView = childView.findViewById(R.id.image);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (lisnter!=null){
            lisnter.onClick();
        }
    }

    public void setLisnter(ImageViewClickLisnter lisnter) {
        this.lisnter = lisnter;
    }

    interface ImageViewClickLisnter{
        void onClick();
    }
}
