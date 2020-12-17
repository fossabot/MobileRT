#ifndef COMPONENTS_LOADERS_OBJLOADER_HPP
#define COMPONENTS_LOADERS_OBJLOADER_HPP

#include "MobileRT/ObjectLoader.hpp"
#include "MobileRT/Sampler.hpp"
#include "MobileRT/Texture.hpp"

#include <map>
#include <tinyobjloader/tiny_obj_loader.h>

namespace Components {
    class OBJLoader final : public ::MobileRT::ObjectLoader {
    private:
        ::std::string objFilePath_ {};
        ::tinyobj::attrib_t attrib_ {};
        ::std::vector<::tinyobj::shape_t> shapes_ {};
        ::std::vector<::tinyobj::material_t> materials_ {};

    public:
        explicit OBJLoader() = delete;

        explicit OBJLoader(::std::string objFilePath, const ::std::string &matFilePath);

        OBJLoader(const OBJLoader &objLoader) = delete;

        OBJLoader(OBJLoader &&objLoader) noexcept = delete;

        ~OBJLoader() final;

        OBJLoader &operator=(const OBJLoader &objLoader) = delete;

        OBJLoader &operator=(OBJLoader &&objLoader) noexcept = delete;

        bool fillScene(::MobileRT::Scene *scene,
                       ::std::function<::std::unique_ptr<::MobileRT::Sampler>()> lambda) final;

    private:
        static ::MobileRT::Texture getTextureFromCache(
            ::std::map<::std::string, ::MobileRT::Texture> *const texturesCache,
            const ::std::string &filePath,
            const ::std::string &texPath);
    };
}//namespace Components

#endif //COMPONENTS_LOADERS_OBJLOADER_HPP
