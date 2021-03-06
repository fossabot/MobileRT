#include "mainwindow.h"
#include <QApplication>

#include "MobileRT/Utils.hpp"
#include <cmath>

int main(int argc, char **argv) {
    /*
     * ${THREAD} ${SHADER} ${SCENE} ${SPP} ${SPL} ${WIDTH} ${HEIGHT} ${ACC} ${REP} \
            ${OBJ} ${MTL} ${CAM} ${PRINT} ${ASYNC} ${SHOWIMAGE}
     */
//    const char* argv[] {"appName",
//        "2", "2", "4", "1", "1", "800", "800", "3", "1",
//        "/mnt/D/Projects/MobileRT/WavefrontOBJs/conference/conference.obj",
//        "/mnt/D/Projects/MobileRT/WavefrontOBJs/conference/conference.mtl",
//        "/mnt/D/Projects/MobileRT/WavefrontOBJs/conference/conference.cam",
//        "true", "true", "true"};
//    argc = 16;

    if (argc != 16) {
        LOG("Wrong number of arguments: ", argc, ", must be 16");
        ::std::exit(1);
    }

    const ::std::int32_t threads {static_cast<::std::int32_t> (strtol(argv[1], nullptr, 0))};
    const ::std::int32_t shader {static_cast<::std::int32_t> (strtol(argv[2], nullptr, 0))};
    const ::std::int32_t scene {static_cast<::std::int32_t> (strtol(argv[3], nullptr, 0))};
    const ::std::int32_t samplesPixel {static_cast<::std::int32_t> (strtol(argv[4], nullptr, 0))};
    const ::std::int32_t samplesLight {static_cast<::std::int32_t> (strtol(argv[5], nullptr, 0))};

    const ::std::int32_t width {
            ::MobileRT::roundDownToMultipleOf(static_cast<::std::int32_t> (strtol(argv[6], nullptr, 0)),
                                              static_cast<::std::int32_t> (::std::sqrt(
                                                      ::MobileRT::NumberOfTiles)))};

    const ::std::int32_t height {
            ::MobileRT::roundDownToMultipleOf(static_cast<::std::int32_t> (strtol(argv[7], nullptr, 0)),
                                              static_cast<::std::int32_t> (::std::sqrt(
                                                      ::MobileRT::NumberOfTiles)))};

    const ::std::int32_t accelerator {static_cast<::std::int32_t> (strtol(argv[8], nullptr, 0))};

    const ::std::int32_t repeats {static_cast<::std::int32_t> (strtol(argv[9], nullptr, 0))};
    const char *const pathObj {argv[10]};
    const char *const pathMtl {argv[11]};
    const char *const pathCam {argv[12]};

    ::std::istringstream ssPrintStdOut (argv[13]);
    ::std::istringstream ssAsync (argv[14]);
    ::std::istringstream ssShowImage (argv[15]);
    bool printStdOut {true};
    bool async {true};
    bool showImage {true};

    ssPrintStdOut >> ::std::boolalpha;
    ssPrintStdOut >> printStdOut;
    ssAsync >> ::std::boolalpha >> async;
    ssShowImage >> ::std::boolalpha >> showImage;
    
    if (!showImage) {
        return 0;
    }

    QApplication application {argc, const_cast<char**> (argv)};
    MainWindow mainWindow {};
    mainWindow.setImage(width, height, threads, shader, scene, samplesPixel, samplesLight,
                        repeats, accelerator, printStdOut, async, pathObj, pathMtl, pathCam);
    mainWindow.show();

    return application.exec();
}
