#ifndef MOBILERT_OBJECTLOADER_HPP
#define MOBILERT_OBJECTLOADER_HPP

#include "MobileRT/Sampler.hpp"
#include "MobileRT/Scene.hpp"
#include "MobileRT/Shapes/Triangle.hpp"
#include <functional>
#include <memory>
#include <string>

namespace MobileRT {
    /**
     * A class which loads a scene from a file and fills the scene with the loaded geometry.
     */
    class ObjectLoader {
    protected:
        bool isProcessed_ {false};
        ::std::int32_t numberTriangles_ {-1};

    public:
        explicit ObjectLoader() = default;

        ObjectLoader(const ObjectLoader &objectLoader) = delete;

        ObjectLoader(ObjectLoader &&objectLoader) noexcept = delete;

        virtual ~ObjectLoader();

        ObjectLoader &operator=(const ObjectLoader &objectLoader) = delete;

        ObjectLoader &operator=(ObjectLoader &&objectLoader) noexcept = delete;

        bool isProcessed() const;

        virtual bool fillScene(Scene *scene,
                               ::std::function<::std::unique_ptr<Sampler>()> lambda) = 0;
    };
}//namespace MobileRT

#endif //MOBILERT_OBJECTLOADER_HPP
