package me.mrfunny.interactionapi.internal.wrapper.resolver.interfaces;

public interface ComplexResolver <T> extends Resolver {
    void resolve();
    T result();
}
