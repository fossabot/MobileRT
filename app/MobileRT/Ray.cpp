#include "MobileRT/Ray.hpp"
#include <atomic>
#include <boost/assert.hpp>

using ::MobileRT::Ray;

namespace {
    /**
     * A helper counter.
     *
     * @return The new value of the counter.
     */
    ::std::int32_t getId() {
        static ::std::atomic<::std::int32_t> counter {};
        const auto currentId {counter.fetch_add(1, ::std::memory_order_relaxed)};
        return currentId;
    }
}//namespace

/**
 * The constructor.
 *
 * @param dir       The direction of the ray.
 * @param origin    The origin point of the ray.
 * @param depth     The number of bounces that the previous ray made.
 * @param primitive The pointer to the primitive where this ray is casted from.
 */
Ray::Ray(const ::glm::vec3 &dir, const ::glm::vec3 &origin,
         const ::std::int32_t depth, const void *const primitive) :
    origin_ {origin},
    direction_ {dir},
    depth_ {depth},
    id_ {getId()},
    primitive_ {primitive} {
        BOOST_ASSERT_MSG(!::glm::all(::glm::isnan(this->direction_)), "direction can't be NaN.");
        BOOST_ASSERT_MSG(!::glm::all(::glm::isinf(this->direction_)), "direction can't be infinite.");
        BOOST_ASSERT_MSG(!equal(this->direction_, ::glm::vec3 {0}), "direction can't be zero.");
}
