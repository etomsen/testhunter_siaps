package it.unibz.testhunter.plugin.hudson.siaps;

import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.plugin.hudson.HudsonApiUrlResolver;
import it.unibz.testhunter.shared.IPlugin;
import it.unibz.testhunter.shared.IPluginProvider;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryProvider;
import com.google.inject.name.Names;

public class PluginModule implements Module, IPluginProvider {

	@Override
	public void configure(Binder binder) {
		binder.bind(IPlugin.class).to(PluginImpl.class);

		binder.bind(IProjectFactory.class).toProvider(
				FactoryProvider.newFactory(IProjectFactory.class,
						ProjectImpl.class));

		binder.bind(IBuildFactory.class).toProvider(
				FactoryProvider
						.newFactory(IBuildFactory.class, BuildImpl.class));

		binder.bind(IJobFactory.class).toProvider(
				FactoryProvider.newFactory(IJobFactory.class, JobImpl.class));

		binder.bind(IUrlResolver.class).annotatedWith(Names.named("project"))
				.to(HudsonApiUrlResolver.class);
		
		binder.bind(IUrlResolver.class).annotatedWith(Names.named("build"))
		.to(BuildVirtualUrlResolver.class);
		
		binder.bind(IUrlResolver.class).annotatedWith(Names.named("job"))
		.to(HudsonApiUrlResolver.class);
	}

	@Override
	public IPlugin get() {
		Injector injector = Guice.createInjector(this);
		return injector.getInstance(IPlugin.class);

	}

}
