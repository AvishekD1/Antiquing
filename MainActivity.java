package com.example.antiquing;

import android.app.AlertDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ArFragment fragment;

    private Uri selectedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        InitializeGallery();

        fragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                    if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING){
                        return;
                    }

                    Anchor anchor = hitResult.createAnchor();

                    placeObject(fragment, anchor, selectedObject);

                }
        );
    }

    private void InitializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

        ImageView chandelier = new ImageView(this);
        chandelier.setImageResource(R.drawable.chandelier);
        chandelier.setContentDescription("Chandelier");
        chandelier.setOnClickListener(view -> {
            selectedObject = Uri.parse("chandelier.sfb");
        });
        gallery.addView(chandelier);

        ImageView gramo = new ImageView(this);
        gramo.setImageResource(R.drawable.gramo);
        gramo.setContentDescription("Gramophone");
        gramo.setOnClickListener(view -> {
            selectedObject = Uri.parse("gramophone.sfb");
        });
        gallery.addView(gramo);

        ImageView pot = new ImageView(this);
        pot.setImageResource(R.drawable.pot);
        pot.setContentDescription("Vase");
        pot.setOnClickListener(view -> {
            selectedObject = Uri.parse("Tower.sfb");
        });
        gallery.addView(pot);

        ImageView paint = new ImageView(this);
        paint.setImageResource(R.drawable.paint);
        paint.setContentDescription("Wall painting");
        paint.setOnClickListener(view -> {
            selectedObject = Uri.parse("Wall_Art_Classical_01.sfb");
        });
        gallery.addView(paint);

        ImageView statue = new ImageView(this);
        statue.setImageResource(R.drawable.statue);
        statue.setContentDescription("Statue");
        statue.setOnClickListener(view -> {
            selectedObject = Uri.parse("12thCenturyCapital.sfb");
        });
        gallery.addView(statue);

    }

    private void placeObject(ArFragment fragment, Anchor anchor, Uri model){

        ModelRenderable.builder()
                .setSource(fragment.getContext(), model)
                .build()
                .thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable))
                .exceptionally((throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage())
                            .setTitle("Error!");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                }));

    }

    private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable){

        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();

    }


}
