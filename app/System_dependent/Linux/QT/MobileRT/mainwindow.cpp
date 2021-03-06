#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "MobileRT/Utils.hpp"
#include <QImage>
#include <QGraphicsPixmapItem>
#include <QTimer>
#include <QFileDialog>
#include "System_dependent/Linux/c_wrapper.h"
#include "about.h"
#include <chrono>
#include <thread>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    m_ui(new Ui::MainWindow)
{
    m_ui->setupUi(this);

    m_graphicsPixmapItem = m_graphicsScene->addPixmap(m_pixmap);
    m_ui->graphicsView->setScene(m_graphicsScene);
    m_ui->graphicsView->show();
}

MainWindow::~MainWindow()
{
    delete m_ui;
}

void MainWindow::exit_app()
{
    stop_render();
    close();
}

void MainWindow::update_image()
{
    draw(m_bitmap, m_width, m_height);
}

void MainWindow::on_actionRender_triggered() {
    restart();
}

void MainWindow::restart() {
    stopRender();
    m_timer->stop();
    disconnect(m_timer, SIGNAL(timeout()));

    ::std::fill(m_bitmap.begin(), m_bitmap.end(), 0);

    RayTrace(m_bitmap.data(), m_width, m_height, m_threads, m_shader, m_scene, m_samplesPixel, m_samplesLight,
             m_repeats, m_accelerator, m_printStdOut, m_async, m_pathObj.c_str(), m_pathMtl.c_str(), m_pathCam.c_str());

    m_timer = new QTimer(this);
    connect(m_timer, SIGNAL(timeout()), this, SLOT(update_image()));
    m_timer->start(1000);
}

void MainWindow::setImage(::std::int32_t width, ::std::int32_t height, ::std::int32_t threads,
                          ::std::int32_t shader, ::std::int32_t scene,
                          ::std::int32_t samplesPixel, ::std::int32_t samplesLight,
                          ::std::int32_t repeats, ::std::int32_t accelerator, bool printStdOut,
                          bool async, const char *pathObj, const char *pathMtl, const char *pathCam) {

    const ::std::uint32_t size {static_cast<::std::uint32_t> (width) * static_cast<::std::uint32_t> (height)};
    m_bitmap = ::std::vector<::std::int32_t> (size);
    m_width = width;
    m_height = height;
    m_threads = threads;
    m_shader = shader;
    m_scene = scene;
    m_samplesPixel = samplesPixel;
    m_samplesLight = samplesLight;
    m_repeats = repeats;
    m_accelerator = accelerator;
    m_printStdOut = printStdOut;
    m_async = async;
    m_pathObj = pathObj;
    m_pathMtl = pathMtl;
    m_pathCam = pathCam;

    RayTrace(m_bitmap.data(), m_width, m_height, m_threads, m_shader, m_scene, m_samplesPixel, m_samplesLight,
             m_repeats, m_accelerator, m_printStdOut, m_async, m_pathObj.c_str(), m_pathMtl.c_str(), m_pathCam.c_str());

    m_timer = new QTimer(this);
    connect(m_timer, SIGNAL(timeout()), this, SLOT(update_image()));
    m_timer->start(1000);

    this->setMinimumSize(width + 2, height + 70);
    m_ui->graphicsView->setMinimumSize(width + 2, height + 2);
}

void MainWindow::keyPressEvent(QKeyEvent *keyEvent) {
    LOG("KEY PRESSED");
    if (keyEvent->key() == ::Qt::Key_Escape) {
        ::QApplication::exit();
    }
}

void MainWindow::draw(const ::std::vector<::std::int32_t> &bitmap, const ::std::int32_t width, const ::std::int32_t height) {
    // ABGR
    const QImage image {
        QImage(
                    reinterpret_cast<const ::std::uint8_t *> (bitmap.data()),
                    width,
                    height,
                    ::QImage::Format::Format_ARGB32
        ).rgbSwapped()
    };
    m_pixmap = ::QPixmap::fromImage(image, ::Qt::NoFormatConversion);
    m_graphicsPixmapItem->setPixmap(m_pixmap);
}

void MainWindow::select_obj()
{
    ::QFileDialog dialog {};
    dialog.setWindowTitle("Select OBJ file");
    dialog.setDirectory("../");
    dialog.setNameFilter("OBJ file (*.obj)");

    if (dialog.exec()) {
        const auto fileName {dialog.selectedFiles().at(0).section(".", 0, 0).toStdString()};
        m_pathObj = fileName + ".obj";
        m_pathMtl = fileName + ".mtl";
        m_pathCam = fileName + ".cam";
    }
    ::std::cout << "m_pathObj: " << m_pathObj << ::std::endl;
}

void MainWindow::select_config()
{
    Config config {};
    if (config.exec()) {
        m_shader = config.getShader();
        m_accelerator = config.getAccelerator();
    }
}

void MainWindow::about()
{
    About about {};
    about.exec();
}

void MainWindow::stop_render()
{
    stopRender();
    m_timer->stop();
    disconnect(m_timer, SIGNAL(timeout()));
    ::std::this_thread::sleep_for(::std::chrono::seconds(1));
}
