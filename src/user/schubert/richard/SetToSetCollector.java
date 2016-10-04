package user.schubert.richard;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collector, der ein Set erzeugt, indem er auf alle Elemente eines gegebenen Sets (jeweils) eine gegebene Funktion ausführt und die Ergebnismengen
 * vereint.
 */
public class SetToSetCollector<T> implements Collector<T, Set<T>, Set<T>>
{
	private Function<T, Set<T>> setProducer;

	public SetToSetCollector(Function<T, Set<T>> setProducer)
	{
		this.setProducer = setProducer;
	}

	@Override
	public Supplier<Set<T>> supplier()
	{
		return () -> new HashSet<T>();
	}

	@Override
	public BiConsumer<Set<T>, T> accumulator()
	{
		return (accumulation, element) ->
		{
			accumulation.addAll(setProducer.apply(element));
		};
	}

	@Override
	public BinaryOperator<Set<T>> combiner()
	{
		return (accumulation1, accumulation2) ->
		{
			accumulation1.addAll(accumulation2);
			return accumulation1;
		};
	}

	@Override
	public Function<Set<T>, Set<T>> finisher()
	{
		return accumulation -> accumulation;
	}

	@Override
	public Set<Characteristics> characteristics()
	{
		return Stream.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED).collect(Collectors.toSet());
	}

}
