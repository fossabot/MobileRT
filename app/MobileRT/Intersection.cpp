#include "MobileRT/Intersection.hpp"

using ::MobileRT::Intersection;

Intersection::Intersection(const float dist, const void *const primitive) noexcept :
    length_ {dist},
    primitive_ {primitive} {
}

Intersection::Intersection(
    const ::glm::vec3 &intPoint,
    const float dist,
    const ::glm::vec3 &normal,
    const void *const primitive,
    const ::std::int32_t materialIndex) noexcept :
    point_ {intPoint},
    normal_ {normal},
    length_ {dist},
    primitive_ {primitive},
    materialIndex_ {materialIndex} {
}
