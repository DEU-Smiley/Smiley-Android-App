package com.example.toothfairy.mlkit


import android.graphics.*
import androidx.annotation.ColorInt
import com.example.smiley.test_ai_assessment.camera.GraphicOverlay
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour


class FaceContourGraphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    companion object {
        // 특징점의 크기
        private const val FACE_POSITION_RADIUS = 6.0f
        private const val ID_TEXT_SIZE = 30.0f
        private const val BOX_STROKE_WIDTH = 5.0f
    }

    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint

    init {
        val selectedColor = Color.WHITE

        facePositionPaint = Paint().apply {
            color = selectedColor
        }

        idPaint = Paint().apply {
            color = selectedColor
            textSize = ID_TEXT_SIZE
        }

        boxPaint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
    }

    private fun Canvas.drawFace(facePosition: Int, @ColorInt selectedColor: Int) {
        val contour = face.getContour(facePosition)
        val path = Path()
        contour?.points?.forEachIndexed { index, pointF ->
            if (index == 0) {
                path.moveTo(
                    translateX(pointF.x),
                    translateY(pointF.y)
                )
            }
            path.lineTo(
                translateX(pointF.x),
                translateY(pointF.y)
            )
        }
        val paint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
        drawPath(path, paint)
    }

    /**
     * 양쪽 눈 끝을 잇는 메소드
     *
     * @param leftEyePosition Int
     * @param rightEyePosition Int
     * @param selectedColor Int
     */
    private fun Canvas.drawFaceEyeLine(leftEyePosition: Int, rightEyePosition:Int, @ColorInt selectedColor: Int) {
        val leftContour = face.getContour(leftEyePosition)
        val rightContour = face.getContour(rightEyePosition)
        val path = Path()

        val leftX = leftContour?.points?.get(0)?.x
        val leftY = leftContour?.points?.get(0)?.y

        val rightX = rightContour?.points?.get(8)?.x
        val rightY = rightContour?.points?.get(8)?.y

        if(leftX != null && leftY != null){
            path.moveTo(
                translateX(leftX),
                translateY(leftY)
            )
        }

        if(rightX != null && rightY != null){
            path.lineTo(
                translateX(rightX),
                translateY(rightY)
            )
        }

        drawPath(path, Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        })
    }

    private fun Canvas.drawLipLine(lipPosition: Int, @ColorInt selectedColor: Int){
        val lip = face.getContour(lipPosition)
        val path = Path()

        val leftX = lip?.points?.get(0)?.x
        val leftY = lip?.points?.get(0)?.y

        val rightX = lip?.points?.get(10)?.x
        val rightY = lip?.points?.get(10)?.y

        path.moveTo(
            translateX(leftX!!),
            translateY(leftY!!)
        )

        path.lineTo(
            translateX(rightX!!),
            translateY(rightY!!)
        )

        drawPath(path, Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        })
    }

    override fun draw(canvas: Canvas?) {

        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            face.boundingBox
        )
        canvas?.drawRect(rect, boxPaint)

        val contours = face.allContours

        contours.forEach {
            it.points.forEach { point ->
                val px = translateX(point.x)
                val py = translateY(point.y)
                canvas?.drawCircle(px, py, FACE_POSITION_RADIUS, facePositionPaint)
            }
        }

        /**
         * drawFace에서 해당 부위의 점들을 선으로 연결해줄 수있음
         * 선 색상 설정 가능
         * 처음 얼굴은 파란색 선으로 다 찍고
         * 반전 시킨 점들은 빨간색 선으로 찍거나 하면 좋을듯
         * 
         * drawFace로 비대칭 판별 라인도 그을 수 있을 듯
         * drawFace(FACE(왼쪽 눈 시작 점, 오른쪽 눈 끝 점),Color.RED) -> 양 쪽 눈 끝을 잇는 선이 그려짐
         */
        canvas?.run {
            // face
            drawFace(FaceContour.FACE, Color.WHITE)

            // left eye (왼쪽 눈 부분)
            drawFace(FaceContour.LEFT_EYEBROW_TOP, Color.WHITE)       // RED
            drawFace(FaceContour.LEFT_EYE, Color.WHITE)             // BLACK
            drawFace(FaceContour.LEFT_EYEBROW_BOTTOM, Color.WHITE)   // CYAN

            // right eye (오른쪽 눈 부분)
            drawFace(FaceContour.RIGHT_EYE, Color.WHITE)           // DKGRAY
            drawFace(FaceContour.RIGHT_EYEBROW_BOTTOM, Color.WHITE)  // GRAY
            drawFace(FaceContour.RIGHT_EYEBROW_TOP, Color.WHITE)    // GREEN

            // nose (코 부분)
            drawFace(FaceContour.NOSE_BOTTOM, Color.WHITE)         // LTGRAY
            drawFace(FaceContour.NOSE_BRIDGE, Color.WHITE)        // MAGENTA

            // rip (입술 부분)
            drawFace(FaceContour.LOWER_LIP_BOTTOM, Color.WHITE)     // WHITE
            drawFace(FaceContour.LOWER_LIP_TOP, Color.WHITE)       // YELLOW
            drawFace(FaceContour.UPPER_LIP_BOTTOM, Color.WHITE)     // GREEN
            drawFace(FaceContour.UPPER_LIP_TOP, Color.WHITE)         // CYAN

            // 눈 양 끝 점 잇기
            drawFaceEyeLine(FaceContour.LEFT_EYE, FaceContour.RIGHT_EYE, Color.BLUE)
            drawFaceEyeLine(FaceContour.LEFT_EYE, FaceContour.RIGHT_EYE, Color.BLUE)

            // 입술 라인 그리기
            drawLipLine(FaceContour.UPPER_LIP_TOP, Color.BLUE)
        }
    }
}