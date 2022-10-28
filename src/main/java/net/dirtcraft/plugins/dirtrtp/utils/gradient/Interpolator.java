package net.dirtcraft.plugins.dirtrtp.utils.gradient;

@FunctionalInterface
public interface Interpolator {

	double[] interpolate(double from, double to, int max);
}
