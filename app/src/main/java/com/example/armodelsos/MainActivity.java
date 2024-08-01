package com.example.armodelsos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArFragment arFragment;
    private ModelRenderable  bearRenderable, lionRenderable;

    ImageView bear, lion;

    View arrayView[];

    int selected = 1; //default oso


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        bear = (ImageView)findViewById(R.id.bear);
        lion = (ImageView)findViewById(R.id.lion);

        setArrayView();
        setClickListener();


        setUpModel();

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {


                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    createModel(anchorNode, selected);

            }
        });

    }

    private void setUpModel() {

        ModelRenderable.builder()
                .setSource(this,R.raw.bear)
                .build()
                .thenAccept(renderable -> bearRenderable = renderable)
                .exceptionally
                        (throwable -> {
                            Toast toast =
                                    Toast.makeText(MainActivity.this,"Model no carga oso", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this,R.raw.lion)
                .build()
                .thenAccept(renderable -> lionRenderable = renderable)
                .exceptionally
                        (throwable -> {
                            Toast toast =
                                    Toast.makeText(MainActivity.this,"Model no carga leon", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            return null;
                        });

    }



    private void createModel(AnchorNode anchorNode, int selected) {
        if(selected == 1);
        {
            TransformableNode bear = new TransformableNode((arFragment.getTransformationSystem()));
            bear.setParent(anchorNode);
            bear.setRenderable(bearRenderable);
            bear.select();

            addName(anchorNode,bear,"Oso");
        }

        if(selected == 2);
        {
            TransformableNode bear = new TransformableNode((arFragment.getTransformationSystem()));
            bear.setParent(anchorNode);
            bear.setRenderable(lionRenderable);
            bear.select();

            addName(anchorNode,bear,"Leon");
        }

    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name) {


        ViewRenderable.builder().setView(this,R.layout.name_animal)
                .build()
                .thenAccept(ViewRenderable ->{


                    TransformableNode nameView = new TransformableNode((arFragment.getTransformationSystem()));
                    nameView.setLocalPosition(new Vector3(0f,model.getLocalPosition().y+0.5f,0));
                    nameView.setParent(anchorNode);
                    nameView.setRenderable(ViewRenderable);
                    nameView.select();

                    //settext
                    TextView txt_name = (TextView)ViewRenderable.getView();
                    txt_name.setText(name);
                    //clic
                    txt_name.setOnClickListener(v -> anchorNode.setParent(null));

                });


    }

    private void setClickListener() {
        for(int i=0;i<arrayView.length;i++)
            arrayView[i].setOnClickListener(this);
    }

    private void setArrayView() {

        arrayView = new View[]{

                bear, lion
        };

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bear){
            selected = 1;
            setBackground(v.getId());
        }

        else if (v.getId() == R.id.lion)
        {
            selected = 2;
        setBackground(v.getId());

        }
    }


    private void setBackground(int id){

        for(int i=0;i<arrayView.length;i++)
        {
            if(arrayView[i].getId() == id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"));
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);
        }
    }

}

