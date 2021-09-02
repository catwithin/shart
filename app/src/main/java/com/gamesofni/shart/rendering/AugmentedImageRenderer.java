  /*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gamesofni.shart.rendering;

import android.content.Context;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;
import com.gamesofni.shart.common.rendering.ObjectRenderer;
import com.gamesofni.shart.common.rendering.ObjectRenderer.BlendMode;

import java.io.IOException;

/** Renders an augmented image. */
public class AugmentedImageRenderer {
  private static final String TAG = "AugmentedImageRenderer";

  private static final float TINT_INTENSITY = 0.1f;
  private static final float TINT_ALPHA = 1.0f;
  private static final int[] TINT_COLORS_HEX = {
    0x000000, 0xF44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4, 0x00BCD4,
    0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFFEB3B, 0xFFC107, 0xFF9800,
  };

  // default frames
//  private final ObjectRenderer imageFrameUpperLeft = new ObjectRenderer();
//  private final ObjectRenderer imageFrameUpperRight = new ObjectRenderer();
//  private final ObjectRenderer imageFrameLowerLeft = new ObjectRenderer();
//  private final ObjectRenderer imageFrameLowerRight = new ObjectRenderer();

  private final ObjectRenderer dragon_yashur = new ObjectRenderer();
  private final ObjectRenderer tibetan_fox = new ObjectRenderer();
  private final ObjectRenderer sonic = new ObjectRenderer();
  private final ObjectRenderer low_fox = new ObjectRenderer();
  private final ObjectRenderer apt = new ObjectRenderer();
  private final ObjectRenderer lighthouse = new ObjectRenderer();
  private final ObjectRenderer king = new ObjectRenderer();
  private final ObjectRenderer king_standing = new ObjectRenderer();
  private final ObjectRenderer owl = new ObjectRenderer();
//  private final ObjectRenderer pub = new ObjectRenderer();
//  private final ObjectRenderer bot_tall = new ObjectRenderer();
//  private final ObjectRenderer ship = new ObjectRenderer();
//  private final ObjectRenderer matilda = new ObjectRenderer();

  public AugmentedImageRenderer() {}

  public void createOnGlThread(Context context) throws IOException {

    // needs experimenting with size probably
//    Log.d("Augm", "creating matilda");
//    matilda.createOnGlThread(
//        context, "models/matilda/matilda.obj", "models/matilda/textures" +
//            "/surfaceShader1_baseColor.png");
//    matilda.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    matilda.setBlendMode(BlendMode.AlphaBlending);

// needs experimenting because of the textures
//    Log.d("Augm", "creating ship");
//    ship.createOnGlThread(
//        context, "models/ship/ship.obj", "models/ship/textures" +
//            "/Boot_Finaal_emissive.jpeg");
//    ship.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    ship.setBlendMode(BlendMode.AlphaBlending);

    dragon_yashur.createOnGlThread(
        context, "models/rafaj_the_mulberry_warlock/rafaj.obj", "models" +
            "/rafaj_the_mulberry_warlock/textures/Rafaj1_baseColor.png");
    dragon_yashur.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    dragon_yashur.setBlendMode(BlendMode.AlphaBlending);

    Log.d("Augm", "creating tibetan fox");
    tibetan_fox.createOnGlThread(
        context, "models/Tibetan_Hill_Fox/tibetan_fox2.obj", "models/Tibetan_Hill_Fox" +
            "/Tibetan_Hill_Fox_dif.jpg");
    tibetan_fox.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    tibetan_fox.setBlendMode(BlendMode.AlphaBlending);


    Log.d("Augm", "creating apt");
    apt.createOnGlThread(
        context, "models/cyberpunk/apt.obj", "models/cyberpunk/textures" +
            "/UV_TEST_baseColor.png");
    apt.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    apt.setBlendMode(BlendMode.AlphaBlending);

    Log.d("Augm", "creating sonic");
    sonic.createOnGlThread(
        context, "models/sonic/sonic.obj", "models/sonic/textures" +
            "/Material_baseColor.png");
    sonic.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    sonic.setBlendMode(BlendMode.AlphaBlending);


    Log.d("Augm", "creating low_fox");
    low_fox.createOnGlThread(
        context, "models/low_fox/low_fox.obj", "models/low_fox" +
            "/texture.png");
    low_fox.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    low_fox.setBlendMode(BlendMode.AlphaBlending);


    Log.d("Augm", "creating lighthouse");
    lighthouse.createOnGlThread(
        context, "models/lighthouse/lighthouse_v.obj", "models/lighthouse/textures" +
            "/lighthouse_low_02_lh_Diffuse2.png");
    lighthouse.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    lighthouse.setBlendMode(BlendMode.AlphaBlending);

    Log.d("Augm", "creating king");
    king.createOnGlThread(
        context, "models/the-owl-house-king/king.obj", "models/the-owl-house-king/textures" +
            "/initialShadingGroup_baseColor.png");
    king.setMaterialProperties(0.0f, 3.5f, 0.2f, 0.1f);
    king.setBlendMode(BlendMode.AlphaBlending);


    Log.d("Augm", "creating owl");
    owl.createOnGlThread(
        context, "models/owl_house_albert_hilt/owl_staff_sm.obj", "models/owl_house_albert_hilt" +
            "/textures" +
            "/wire_177088027_baseColor.png");
    owl.setMaterialProperties(0.0f, 3.5f, 0.2f, 1.0f);
    owl.setBlendMode(BlendMode.AlphaBlending);

    // default angles
//    Log.d("Augm", "creating angles");
//    imageFrameLowerLeft.createOnGlThread(
//        context, "models/frame_lower_left.obj", "models/frame_base.png");
//    imageFrameLowerLeft.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    imageFrameLowerLeft.setBlendMode(BlendMode.AlphaBlending);

//    imageFrameUpperRight.createOnGlThread(
//        context, "models/frame_upper_right.obj", "models/frame_base.png");
//    imageFrameUpperRight.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    imageFrameUpperRight.setBlendMode(BlendMode.AlphaBlending);



    Log.d("Augm", "creating another");
    king_standing.createOnGlThread(
        context, "models/king/king2.obj", "models/king" +
            "/textures" +
            "/king_baseColor.png");
    king_standing.setMaterialProperties(0.0f, 3.5f, 0.2f, 1.0f);
    king_standing.setBlendMode(BlendMode.AlphaBlending);


// works, but too big
//    Log.d("Augm", "creating pub");
//    pub.createOnGlThread(
//        context, "models/pub/pub_sm.obj", "models/pub/textures" +
//            "/Birbs_final_Material.001_color.png");
//    pub.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    pub.setBlendMode(BlendMode.AlphaBlending);


    // works, but without glass, and too big
//    Log.d("Augm", "creating tall bot");
//    bot_tall.createOnGlThread(
//        context, "models/terrarium_bots/bot_tall.obj", "models/terrarium_bots/textures" +
//            "/BOT1_baseColor.jpeg");
//    bot_tall.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    bot_tall.setBlendMode(BlendMode.AlphaBlending);

  }

  public void draw(
      float[] viewMatrix,
      float[] projectionMatrix,
      AugmentedImage augmentedImage,
      Anchor centerAnchor,
      float[] colorCorrectionRgba) {

    // coloring of the default frame
    //    float[] tintColor =
//        convertHexToColor(TINT_COLORS_HEX[augmentedImage.getIndex() % TINT_COLORS_HEX.length]);

//    Pose[] localBoundaryPoses = {
//      Pose.makeTranslation(
//          -0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          -0.5f * augmentedImage.getExtentZ()), // upper left
//      Pose.makeTranslation(
//          0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          -0.5f * augmentedImage.getExtentZ()), // upper right
//      Pose.makeTranslation(
//          0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          0.5f * augmentedImage.getExtentZ()), // lower right
//      Pose.makeTranslation(
//          -0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          0.5f * augmentedImage.getExtentZ()) // lower left
//    };

    Pose anchorPose = centerAnchor.getPose();
//    Pose[] worldBoundaryPoses = new Pose[4];
//    for (int i = 0; i < 4; ++i) {
//      worldBoundaryPoses[i] = anchorPose.compose(localBoundaryPoses[i]);
//    }

    float scaleFactor = 1.0f;
    float[] modelMatrix = new float[16];

//    worldBoundaryPoses[0].toMatrix(modelMatrix, 0);
    anchorPose.toMatrix(modelMatrix, 0);

    ObjectRenderer chosenModel = null;

    switch (augmentedImage.getIndex()) {

      case 28: // the pocket mirror
        chosenModel = apt; //apt
        break;
      case 29: // fox on the carton box
        chosenModel = tibetan_fox; // tibetan_fox
        break;

      case 34: // cat from York
        chosenModel = sonic; //sonic
        break;

      case 36: //  postcard fox
        chosenModel = low_fox; // low_fox
        break;

      case 27: // owl badge
        chosenModel = owl;
        break;

//      case 30: // TODO girl with the tea - postcard
//        chosenModel = pub;
//        break;
//      case 31: // TODO owl on the carton box
//        chosenModel = bot_tall;
//        break;

      case 32: //  blue and gold painting
        chosenModel = dragon_yashur;
        break;

      case 33: //  dune poster
        chosenModel = lighthouse;
        break;

      case 39: //  yellow flower painting in the living room
        chosenModel = king;
        break;

//      case 40: //  coop
//        chosenModel = bot_tall;
//        break;

      default: // TODO del
        chosenModel = king_standing;
        break;

    }

    if (chosenModel == null) {
      return;
    }
    chosenModel.updateModelMatrix(modelMatrix, scaleFactor);
    chosenModel.draw(viewMatrix, projectionMatrix, colorCorrectionRgba);

//    worldBoundaryPoses[1].toMatrix(modelMatrix, 0);
//    imageFrameUpperRight.updateModelMatrix(modelMatrix, scaleFactor);
//    imageFrameUpperRight.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
//
//    worldBoundaryPoses[2].toMatrix(modelMatrix, 0);
//    imageFrameLowerRight.updateModelMatrix(modelMatrix, scaleFactor);
//    imageFrameLowerRight.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
//
//    worldBoundaryPoses[3].toMatrix(modelMatrix, 0);
//    imageFrameLowerLeft.updateModelMatrix(modelMatrix, scaleFactor);
//    imageFrameLowerLeft.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
  }

  private static float[] convertHexToColor(int colorHex) {
    // colorHex is in 0xRRGGBB format
    float red = ((colorHex & 0xFF0000) >> 16) / 255.0f * TINT_INTENSITY;
    float green = ((colorHex & 0x00FF00) >> 8) / 255.0f * TINT_INTENSITY;
    float blue = (colorHex & 0x0000FF) / 255.0f * TINT_INTENSITY;
    return new float[] {red, green, blue, TINT_ALPHA};
  }
}
