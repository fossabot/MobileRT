#ifndef COMPONENTS_LIGHTS_AREALIGHT_HPP
#define COMPONENTS_LIGHTS_AREALIGHT_HPP

#include "MobileRT/Light.hpp"
#include "MobileRT/Sampler.hpp"
#include "MobileRT/Shapes/Triangle.hpp"
#include "MobileRT/Utils.hpp"
#include <memory>

namespace Components {

    class AreaLight final : public ::MobileRT::Light {
    private:
        ::MobileRT::Triangle triangle_;
        ::std::unique_ptr<::MobileRT::Sampler> samplerPointLight_ {};

    public:
        explicit AreaLight() = delete;

        explicit AreaLight(
            const ::MobileRT::Material &radiance,
            ::std::unique_ptr<::MobileRT::Sampler> samplerPointLight,
            const ::MobileRT::Triangle &triangle);

        AreaLight(const AreaLight &areaLight) = delete;

        AreaLight(AreaLight &&areaLight) noexcept = delete;

        ~AreaLight() final = default;

        AreaLight &operator=(const AreaLight &areaLight) = delete;

        AreaLight &operator=(AreaLight &&areaLight) noexcept = delete;

        ::glm::vec3 getPosition() final;

        void resetSampling() final;

        ::MobileRT::Intersection intersect(
            ::MobileRT::Intersection intersection,
            const ::MobileRT::Ray &ray) final;
    };
}//namespace Components

#endif //COMPONENTS_LIGHTS_AREALIGHT_HPP
