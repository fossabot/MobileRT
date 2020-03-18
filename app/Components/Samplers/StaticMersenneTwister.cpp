#include "Components/Samplers/StaticMersenneTwister.hpp"
#include <array>

using ::Components::StaticMersenneTwister;

namespace {
    ::std::array<float, ::MobileRT::ArraySize> values {};
}//namespace

StaticMersenneTwister::StaticMersenneTwister() {
    ::MobileRT::fillArrayWithMersenneTwister(&values);
}

float StaticMersenneTwister::getSample(const ::std::uint32_t /*sample*/) {
    return Sampler::getSampleFromArray(values);
}
