package com.java.common.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码
 */
@Service
public class QrcodeService {

    private Logger logger = LoggerFactory.getLogger(QrcodeService.class);

    /**
     * 生成二维码
     *
     * @param height
     * @param width
     * @param content
     * @return
     * @throws Exception
     */
    public void qrcode(Integer height, Integer width, String content, OutputStream outputStream) throws Exception {
        /** 定义Map集合封装二维码需要全局配置信息 */
        Map<EncodeHintType, Object> hints = new HashMap<>();
        /** 设置二维码图片中内容编码 */
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        /** 设置二维码图片上下左右的边距 */
        hints.put(EncodeHintType.MARGIN, 0);
        /** 设置二维码的纠错级别 */
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        /**
         * 创建二维码字节转换对象
         * 第一个参数：二维码图片中的内容
         * 第二个参数：二维码的格式器
         * 第三个参数：二维码的宽度
         * 第四个参数：二维码的高度
         * 第五个参数：二维码的全局配置信息
         */
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        /** 获取二维码实际宽度 */
        int matrixWidth = matrix.getWidth();
        /** 获取二维码实际高度 */
        int matrixHeight = matrix.getHeight();

        /** 创建缓冲流图片(空白) */
        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
        /** 把二维码字节转换对象中的内容绘制缓冲流图片 */
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                /** 获取一个像点，判断是白色点还是黑色的点  true : 黑色点   false: 白色点 */
                int rgb = matrix.get(x, y) ? 0x000000 : 0xffffff;
                image.setRGB(x, y, rgb);
            }
        }

        ImageIO.write(image, "png", outputStream);
    }

}
