package com.example.myfirstapkproject;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Vector;



class DrawView extends View{

    Paint paint;
    Vector<Dot> black, white;
    final int strokeWidth, numberOfLines;
    float multiplier, height, width;
    MyLine line;
    float x, y;
    String s;

    public DrawView(Context context, AttributeSet attr) {
        super(context, attr);
        paint = new Paint();
        black = new Vector<Dot>();
        white = new Vector<Dot>();
        strokeWidth = 10;
        multiplier = 10;
        numberOfLines = 10;
        height = width = 0;
        x = y = 0;
        s = "";
        line = new MyLine(new Dot(0.0, 0.0), new Dot(0.0,0.0), false);

    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                s = "Down";
                this.invalidate();
                break;
            case MotionEvent.ACTION_MOVE: // движение
                s = "Move";
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                s = "Up";
                this.invalidate();
                break;
        }

        return false;
    }*/

    @Override
    protected void onDraw(Canvas canvas){
        height = canvas.getHeight();
        float heightPercent = height / 100;
        width = canvas.getWidth();
        float widthPercent = width / 100;
        canvas.drawColor(Color.rgb(240,240,240));

        paint.setColor(Color.RED);
        paint.setTextSize(24);



        paint.setColor(Color.LTGRAY);

        for(int i = 1; i < numberOfLines; i++){
            //Drawing Y-gray line
            canvas.drawLine((canvas.getWidth()/numberOfLines)*i, canvas.getHeight(),
                    (canvas.getWidth()/numberOfLines)*i, strokeWidth, paint);
            canvas.drawText( Integer.toString((int)multiplier*i), (float)(canvas.getWidth()/numberOfLines)*i, canvas.getHeight() - 10.0f, paint);
            //Drawing X-gray line
            canvas.drawLine(0, canvas.getHeight()-i*canvas.getHeight()/numberOfLines,
                    canvas.getWidth(), canvas.getHeight()-i*canvas.getHeight()/numberOfLines, paint);
            canvas.drawText(Integer.toString((int)multiplier*i),
                    10.0f, canvas.getHeight()-i*canvas.getHeight()/numberOfLines, paint);
        }

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(strokeWidth);
        //Drawing Y line
        canvas.drawLine(0, canvas.getHeight(), 0, strokeWidth, paint);
        //Drawing X line
        canvas.drawLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), paint);



        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(3);
        for(int i = 0; i < black.size(); i++)
            canvas.drawCircle((int)black.get(i).getX()*canvas.getWidth()/(numberOfLines*multiplier),
                    (int)(canvas.getHeight()-black.get(i).getY()*canvas.getHeight()/(numberOfLines*multiplier)), strokeWidth, paint);
        paint.setStyle(Paint.Style.STROKE);

        for(int i = 0; i < white.size(); i++) {
            canvas.drawCircle((int) white.get(i).getX() * canvas.getWidth() / (numberOfLines * multiplier),
                    (int) (canvas.getHeight() - white.get(i).getY() * canvas.getHeight() / (numberOfLines * multiplier)), strokeWidth, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle((int) white.get(i).getX() * canvas.getWidth() / (numberOfLines * multiplier),
                    (int) (canvas.getHeight() - white.get(i).getY() * canvas.getHeight() / (numberOfLines * multiplier)), strokeWidth/1.5f, paint);
            paint.setColor(Color.BLACK);
        }



        if(line.firstPoint.getX() - line.secondPoint.getX() == 0){
            canvas.drawLine((float)line.firstPoint.getX()*widthPercent,
                    0.0f,
                    (float)line.firstPoint.getX()*widthPercent,
                    height, paint);
        } else {
            if(line.firstPoint.getY() - line.secondPoint.getY() == 0){
                canvas.drawLine(0.0f,
                        height - (float)line.firstPoint.getY()*heightPercent,
                        width,
                        height - (float)line.firstPoint.getY()*heightPercent, paint);
            } else {
                canvas.drawLine(0.0f, height - (float) line.getY(0.0, height) * heightPercent, width,
                        height - (float) line.getY(100.0, height) * heightPercent, paint);
            }
        }



        paint.setStrokeWidth(3);
        canvas.drawText( " y = x * " + Double.toString(line.k) + " + " + Double.toString(line.b), 50.0f, 50.0f, paint);
        canvas.drawText( Double.toString(line.A) +
                "*x + " + Double.toString(line.B) + "*y + " + Double.toString(line.C) + " = 0", 50.0f, 100.0f, paint);
    }

    protected void drawDot(Vector<Dot> black, Vector<Dot> white){
        this.black = black;
        this.white = white;
        invalidate();
    }

    protected void drawLine(MyLine line) {
        this.line = line;
        invalidate();
    }


}



class MyLine{
    Dot firstPoint;
    Dot secondPoint;

    /* Уравнение A*x + B*y + C = 0 - уравнение прямой*/

    public final double A;
    public final double B;
    public final double C;
    public final double k;
    public final double b;

    final boolean isWhite;//Если линия включает в себя белые точки

    MyLine(Dot first, Dot second, boolean isWhite){
        final double x1 = first.getX();
        final double x2 = second.getX();
        final double y1 = first.getY();
        final double y2 = second.getY();
        firstPoint = first;
        secondPoint = second;
        this.isWhite = isWhite;
        k = (y2 - y1)/(x2-x1);
        b = (-(x1*(y2-y1))/(x2 - x1) + y1);  //y = k*x - b;
        A = (y1 - y2);
        B = (x2 - x1);
        C = (x1*y2 - x2*y1);
    }
    /*Сначала проверяем белые точки, потом черные*/

    MyLine makeClosestLine(Vector<Dot> white, Vector<Dot> black){
        Vector<Dot> vec;
        if(isWhite) {
            vec = black;
        } else {
            vec = white;
        }
        double d =
                (Math.abs(A*vec.get(0).getX() + B*vec.get(0).getY() + C)/Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2)));
        Dot minDistanceDot = vec.get(0);
        for(int i = 1; i < vec.size(); i++){
            double a =
                    Math.abs(A*vec.get(i).getX() + vec.get(i).getY()*B + C)/Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
            if(a < d){
                d = a;
                minDistanceDot = vec.get(i);
            }
        }
        double x1 = (firstPoint.getX() + minDistanceDot.getX()) / 2.0;
        double y1 = (firstPoint.getY() + minDistanceDot.getY()) / 2.0;
        double x2 = (secondPoint.getX() + minDistanceDot.getX()) / 2.0;
        double y2 = (secondPoint.getY() + minDistanceDot.getY()) / 2.0;
        return new MyLine(new Dot(x1, y1),new Dot(x2, y2), isWhite );
    }

    boolean isDividing(Vector<Dot> white, Vector<Dot> black) {
        int successCounter = 0;
        double x1 = firstPoint.getX();
        double x2 = secondPoint.getX();
        double y1 = firstPoint.getY();
        double y2 = secondPoint.getY();
        for (int i = 0; i < white.size(); i++) {
            double x3 = white.get(i).getX();
            double y3 = white.get(i).getY();
            double d = (y3 - y1) * (x2 - x1) - (x3 - x1) * (y2 - y1);
            System.out.print("D: " + d + " " + x1 + " " + y1 + " " + x2 + " " + y2 + " "+ x3 + " " + y3 + " \n");
            if (isWhite) {
                if (d >= 0) {
                    successCounter++;
                } else {
                    successCounter--;
                }
            } else {
                if (d > 0) {
                    successCounter++;
                } else {
                    if(d < 0) {
                        successCounter--;
                    }
                }
            }
        }
        System.out.print("SuccCount: " + successCounter + " \n");
        for (int i = 0; i < black.size(); i++) {
            double x3 = black.get(i).getX();
            double y3 = black.get(i).getY();
            double d =  (y3 - y1) * (x2 - x1) - (x3 - x1) * (y2 - y1);
            System.out.print("D: " + d + " " + x1 + " " + y1 + " " + x2 + " " + y2 + " "+ x3 + " " + y3 + " \n");
            if (!isWhite) {
                if (d >= 0) {
                    successCounter--;
                } else {
                    successCounter++;
                }
            } else {
                if (d > 0) {
                    successCounter--;
                } else {
                    if(d < 0) {
                        successCounter++;
                    }
                }
            }
        }
        System.out.print("SuccCount: " + successCounter + " \n");
        return successCounter == (white.size() + black.size())
                || (-successCounter) == (white.size() + black.size());
    }


    double getY(double x, float height){
        float heightPercent = height / 100;
        if ((secondPoint.getX() - firstPoint.getX()) != 0) {
            return ((x * k) + (b));
        } else {
            if(x == 0) {
                return height;
            }
            else{
                return 0;
            }
        }
    }
}

class Dot{
    private final double x,y;
    Dot(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapkproject.MESSAGE";
    private Vector<Dot> black, white;
    //private float x, y;
    private MyLine line;
    final int maxCoord = 100;
    final int range = 30;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        black = new Vector<>();
        white = new Vector<Dot>();
        line = new MyLine(new Dot(0,0), new Dot(0,0), true);
    //    x = y = 0;
        final DrawView drawView = (DrawView) findViewById(R.id.drawView);
        final MainActivity ths = this;
        drawView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                if (event.getAction() == MotionEvent.ACTION_DOWN) // нажатие
                {
                    boolean isWhite = false;
                    boolean isAlreadyExists = false;
                    int index = 0;
                    for (int i = 0; i < black.size(); i++)
                        if (y-range <= black.get(i).getY() && y+range >= black.get(i).getY())
                            if (x-range <= black.get(i).getX() && x+range >= black.get(i).getX()){
                                isAlreadyExists = true;
                                isWhite = true;
                                index = i;
                                break;
                            }
                    if(!isAlreadyExists) {
                        for (int i = 0; i < white.size(); i++)
                            if (y-range <= white.get(i).getY() && y+range >= white.get(i).getY())
                                if (x-range <= white.get(i).getX() && x+range >= white.get(i).getX()){
                                    isAlreadyExists = true;
                                    index = i;
                                    break;
                                }
                    }
                    if (!isAlreadyExists) {
                        ths.drawDot(maxCoord * (int) x / drawView.getWidth(),
                                maxCoord - maxCoord * (int) y / drawView.getHeight(),
                                (DrawView) findViewById(R.id.drawView));
                       /* Toast message = Toast.makeText(getApplicationContext(),
                                "Точка поставлена ( " + maxCoord * (int) x / drawView.getWidth() +
                                        " , " + (maxCoord - maxCoord * (int) y / drawView.getHeight())
                                + " )!"
                                , Toast.LENGTH_SHORT);
                        message.show();*/
                    }
                    if (isAlreadyExists){
                        if(isWhite){
                            white.remove(index);
                        } else{
                            black.remove(index);
                        }
                    }
                }
                return true;
            }
        });
    }



    /*public void sendMessage(View view){
        Intent intent = new Intent(this, DisMesActivity.class );
        EditText edit = (EditText)findViewById(R.id.editText);
        String message = edit.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

    public void drawLine(View view){
        if(white.size() < 2 || black.size() < 2){
            Toast message = Toast.makeText(getApplicationContext(), "Должно быть минимум 2 точки каждого цвета!", Toast.LENGTH_LONG);
            message.show();
        } else {
            DrawView drawView = (DrawView) findViewById(R.id.drawView);
            MyLine line = null;
            boolean foundLine = false;
            for(int i = 0; i < white.size(); i++){
                if(foundLine){
                    break;
                }
                for(int j = i + 1; j < white.size(); j++){
                    line = new MyLine(white.get(i), white.get(j), true);
                    if(line.isDividing(white, black)){
                        foundLine = true;
                        break;
                    } else {    //Попробовать линию наоборот (Баг в случае, если линия вертикальна и порядок точек другой)
                        line = new MyLine(white.get(j), white.get(i), true);
                        if(line.isDividing(white, black)) {
                            foundLine = true;
                            break;
                        }
                    }
                }
            }
            for(int i = 0; i < black.size(); i++){
                if(foundLine){
                    break;
                }
                for(int j = i + 1; j < black.size(); j++){
                    line = new MyLine(black.get(i), black.get(j), false);
                    if(line.isDividing(white, black)){
                        foundLine = true;
                        break;
                    } else {  //Попробовать линию наоборот (Баг в случае, если линия вертикальна и порядок точек другой)
                        line = new MyLine(black.get(j), black.get(i), true);
                        if(line.isDividing(white, black)) {
                            foundLine = true;
                            break;
                        }
                    }
                }
            }
            if(foundLine){

                Toast message = Toast.makeText(getApplicationContext(), "Вы нашли линию!", Toast.LENGTH_LONG);
                message.show();
                /*
                message = Toast.makeText(getApplicationContext(), "Линия:"
                        + " Первая точка: " + line.firstPoint.getX() + " , " + line.firstPoint.getY() + ";\n"
                        + " Вторая точка: " + line.secondPoint.getX() + " , " + line.secondPoint.getY() + ";\n"
                        + " Цвет линии: " + line.isWhite, Toast.LENGTH_LONG);
                message.show();
                message = Toast.makeText(getApplicationContext(), "Линия:"
                        + " Первая точка: 0.0  " + " , " + line.getYifXisZero() + ";\n"
                        + " Вторая точка: " + line.getXifYisZero() + " , 0.0 ;\n", Toast.LENGTH_LONG);
                message.show();*/
                line = line.makeClosestLine(white, black);
                /*message = Toast.makeText(getApplicationContext(), "Линия:"
                        + " Первая точка: " + line.firstPoint.getX() + " , " + line.firstPoint.getY() + ";\n"
                        + " Вторая точка: " + line.secondPoint.getX() + " , " + line.secondPoint.getY() + ";\n"
                        + " Цвет линии: " + line.isWhite, Toast.LENGTH_LONG);
                message.show();*/
                drawView.drawLine(line);
                drawView.invalidate();
            }
            else{
                Toast message = Toast.makeText( getApplicationContext(),"Линия не найдена:с" + " " + line.k + " " + line.b, Toast.LENGTH_LONG);
                message.show();
            }
        }
    }

    public void createButton(View view){
        DrawView drawView = (DrawView) findViewById(R.id.drawView);
        EditText editX = (EditText)findViewById(R.id.xCord);
        EditText editY = (EditText)findViewById(R.id.yCord);
        int x = Integer.parseInt(editX.getText().toString());
        int y = Integer.parseInt(editY.getText().toString());
        drawDot(x , y, drawView);
    }

    private void drawDot(int x, int y, DrawView drawView){
        boolean isAlreadyExists = false;
        if(x > maxCoord || y > maxCoord || x < 0 || y < 0){
            Toast message = Toast.makeText(getApplicationContext(), "Координаты не могут быть больше " + maxCoord + " или отрицательными!", Toast.LENGTH_LONG);
            message.show();
        } else {
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadGroup);
            if (radioGroup.getCheckedRadioButtonId() == findViewById(R.id.radioBlack).getId()) {
                for (int i = 0; i < black.size(); i++)
                    if (y == black.get(i).getY())
                        if (x == black.get(i).getX()) {
                            isAlreadyExists = true;
                            Toast message = Toast.makeText(getApplicationContext(), "Такая черная точка уже существует!", Toast.LENGTH_LONG);
                            message.show();
                        }
                if (!isAlreadyExists) black.add(new Dot(x, y));
            }
            if (radioGroup.getCheckedRadioButtonId() == findViewById(R.id.radioWhite).getId()) {
                for (int i = 0; i < white.size(); i++)
                    if (y == white.get(i).getY())
                        if (x == white.get(i).getX()) {
                            isAlreadyExists = true;
                            Toast message = Toast.makeText(getApplicationContext(), "Такая белая точка уже существует!", Toast.LENGTH_LONG);
                            message.show();
                        }
                if (!isAlreadyExists) white.add(new Dot(x, y));
            }
            drawView.drawDot(black, white);

        }
    }

}
