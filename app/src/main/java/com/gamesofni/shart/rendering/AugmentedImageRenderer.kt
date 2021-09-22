package com.gamesofni.shart.rendering

import android.content.Context
import android.util.Log
import com.gamesofni.shart.SearchActivity
import com.gamesofni.shart.common.rendering.ObjectRenderer
import com.gamesofni.shart.data.Model3d
import kotlin.Throws
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Anchor
import java.io.IOException
import com.google.ar.core.Pose;

/** Renders an augmented image.  */
class AugmentedImageRenderer(
    private val models: List<Model3d>)

//  private final ObjectRenderer pub = new ObjectRenderer();
//  private final ObjectRenderer bot_tall = new ObjectRenderer();
//  private final ObjectRenderer ship = new ObjectRenderer();
//  private final ObjectRenderer matilda = new ObjectRenderer();
{
    // default frames
    //  private final ObjectRenderer imageFrameUpperLeft = new ObjectRenderer();
    //  private final ObjectRenderer imageFrameUpperRight = new ObjectRenderer();
    //  private final ObjectRenderer imageFrameLowerLeft = new ObjectRenderer();
    //  private final ObjectRenderer imageFrameLowerRight = new ObjectRenderer();



//    private val dragon_yashur = ObjectRenderer()
//    private val tibetan_fox = ObjectRenderer()
//    private val sonic = ObjectRenderer()
//    private val low_fox = ObjectRenderer()
//    private val apt = ObjectRenderer()
//    private val lighthouse = ObjectRenderer()
//    private val king = ObjectRenderer()
//    private val king_standing = ObjectRenderer()
//    private val owl = ObjectRenderer()

    private var renderers = mutableListOf<ObjectRenderer>()

    @Throws(IOException::class)
    fun createOnGlThread(context: Context) {
//        Log.e(TAG, "AugmentedImgRenderer:::in createOnGLThread::::")
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

//        Log.e(TAG, "IN::: createOnGlThread::::")
//        Log.e(TAG, "models::::" + models.get(0))

        for (m in models) {
            val renderer = ObjectRenderer()
            renderer.createOnGlThread(
                context, m.filenameObj, m.textureResourcePath
            )
            // TODO: also save this info in model
            renderer.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
            renderer.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)
            renderers.add(renderer)
        }




//        dragon_yashur.createOnGlThread(
//            context, "models/rafaj_the_mulberry_warlock/rafaj.obj", "models" +
//                    "/rafaj_the_mulberry_warlock/textures/Rafaj1_baseColor.png"
//        )
//        dragon_yashur.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
//        dragon_yashur.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)

//        Log.d("Augm", "creating tibetan fox")
//        tibetan_fox.createOnGlThread(
//            context, "models/Tibetan_Hill_Fox/tibetan_fox2.obj", "models/Tibetan_Hill_Fox" +
//                    "/Tibetan_Hill_Fox_dif.jpg"
//        )
//        tibetan_fox.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
//        tibetan_fox.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)
//        Log.d("Augm", "creating apt")
//        apt.createOnGlThread(
//            context, "models/cyberpunk/apt.obj", "models/cyberpunk/textures" +
//                    "/UV_TEST_baseColor.png"
//        )
//        apt.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
//        apt.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)
//        Log.d("Augm", "creating sonic")
//        sonic.createOnGlThread(
//            context, "models/sonic/sonic.obj", "models/sonic/textures" +
//                    "/Material_baseColor.png"
//        )
//        sonic.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
//        sonic.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)
//        Log.d("Augm", "creating low_fox")
//        low_fox.createOnGlThread(
//            context, "models/low_fox/low_fox.obj", "models/low_fox" +
//                    "/texture.png"
//        )
//        low_fox.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
//        low_fox.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)
//        Log.d("Augm", "creating lighthouse")
//        lighthouse.createOnGlThread(
//            context, "models/lighthouse/lighthouse_v.obj", "models/lighthouse/textures" +
//                    "/lighthouse_low_02_lh_Diffuse2.png"
//        )
//        lighthouse.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f)
//        lighthouse.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)
//        Log.d("Augm", "creating king")
//        king.createOnGlThread(
//            context, "models/the-owl-house-king/king.obj", "models/the-owl-house-king/textures" +
//                    "/initialShadingGroup_baseColor.png"
//        )
//        king.setMaterialProperties(0.0f, 3.5f, 0.2f, 0.1f)
//        king.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)
//        Log.d("Augm", "creating owl")
//        owl.createOnGlThread(
//            context,
//            "models/owl_house_albert_hilt/owl_staff_sm.obj",
//            "models/owl_house_albert_hilt" +
//                    "/textures" +
//                    "/wire_177088027_baseColor.png"
//        )
//        owl.setMaterialProperties(0.0f, 3.5f, 0.2f, 1.0f)
//        owl.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)

//        Log.d("Augm", "creating another")
//        king_standing.createOnGlThread(
//            context, "models/king/king2.obj", "models/king" +
//                    "/textures" +
//                    "/king_baseColor.png"
//        )
//        king_standing.setMaterialProperties(0.0f, 3.5f, 0.2f, 1.0f)
//        king_standing.setBlendMode(ObjectRenderer.BlendMode.AlphaBlending)








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

    fun draw(
        viewMatrix: FloatArray,
        projectionMatrix: FloatArray,
        augmentedImage: AugmentedImage,
        model: Model3d?,
        centerAnchor: Anchor,
        colorCorrectionRgba: FloatArray
    ) {

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
        val anchorPose : Pose = centerAnchor.pose
        //    Pose[] worldBoundaryPoses = new Pose[4];
//    for (int i = 0; i < 4; ++i) {
//      worldBoundaryPoses[i] = anchorPose.compose(localBoundaryPoses[i]);
//    }
        val scaleFactor = 1.0f
        val modelMatrix = FloatArray(16)
//                Log.e(TAG, "modelMatrix::BEFORE:" + modelMatrix)
//    worldBoundaryPoses[0].toMatrix(modelMatrix, 0);
        anchorPose.toMatrix(modelMatrix, 0)
//        var chosenModel: ObjectRenderer? = null
//        Log.e(TAG, "modelMatrix::AFTER:" + modelMatrix)

//        king_standing.updateModelMatrix(modelMatrix, scaleFactor)
//        king_standing.draw(viewMatrix, projectionMatrix, colorCorrectionRgba)

        // TODO: pass only id, save map { id -> model }
        val index = models.indexOf(model)

//        Log.e(TAG, "model:::" + model)
//        Log.e(TAG, "index:::" + index)

        if (index == -1) {
            return
        }

//        chosenModel = when (augmentedImage.index) {
//            28 -> apt //apt
//            29 -> tibetan_fox // tibetan_fox
//            34 -> sonic //sonic
//            36 -> low_fox // low_fox
//            27 -> owl
//            32 -> dragon_yashur
//            33 -> lighthouse
//            39 -> king
//            else -> king_standing
//        }

        val chosenRenderer = renderers.get(index)
//        Log.e(TAG, "chosenRenderer:::" + chosenRenderer)
        chosenRenderer.updateModelMatrix(modelMatrix, scaleFactor)
        chosenRenderer.draw(viewMatrix, projectionMatrix, colorCorrectionRgba)



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

    companion object {
        private const val TAG = "AugmentedImageRenderer"
        private const val TINT_INTENSITY = 0.1f
        private const val TINT_ALPHA = 1.0f
        private val TINT_COLORS_HEX = intArrayOf(
            0x000000,
            0xF44336,
            0xE91E63,
            0x9C27B0,
            0x673AB7,
            0x3F51B5,
            0x2196F3,
            0x03A9F4,
            0x00BCD4,
            0x009688,
            0x4CAF50,
            0x8BC34A,
            0xCDDC39,
            0xFFEB3B,
            0xFFC107,
            0xFF9800
        )

        private fun convertHexToColor(colorHex: Int): FloatArray {
            // colorHex is in 0xRRGGBB format
            val red = (colorHex and 0xFF0000 shr 16) / 255.0f * TINT_INTENSITY
            val green = (colorHex and 0x00FF00 shr 8) / 255.0f * TINT_INTENSITY
            val blue = (colorHex and 0x0000FF) / 255.0f * TINT_INTENSITY
            return floatArrayOf(red, green, blue, TINT_ALPHA)
        }
    }
}