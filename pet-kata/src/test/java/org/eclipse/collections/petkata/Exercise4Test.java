/*
 * Copyright (c) 2016 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.petkata;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.factory.primitive.IntSets;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.test.Verify;
import org.eclipse.collections.impl.tuple.primitive.PrimitiveTuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * In this set of tests, wherever you see .stream() replace it with an Eclipse Collections alternative
 */
public class Exercise4Test extends PetDomainForKata
{
    @Test
    public void getAgeStatisticsOfPets()
    {
        // Try to use a MutableIntList here instead
        // Hints: flatMap = flatCollect, map = collect, mapToInt = collectInt
        MutableIntList petAges = this.people.flatCollect(Person::getPets)
                .collectInt(Pet::getAge);

        // Try to use an IntSet here instead
        MutableIntSet uniqueAges = petAges.toSet();
        // IntSummaryStatistics is a class in JDK 8 - Try and use it with MutableIntList.forEach()
        IntSummaryStatistics stats = new IntSummaryStatistics();
        petAges.forEach(i -> stats.accept(i));
        // Is a Set<Integer> equal to an IntSet?
        // Hint: Try IntSets instead of Sets as the factory
        Assert.assertEquals(IntSets.mutable.with(1, 2, 3, 4), uniqueAges);
        // Try to leverage min, max, sum, average from the Eclipse Collections primitive api 
        Assert.assertEquals(stats.getMin(), petAges.min());
        Assert.assertEquals(stats.getMax(), petAges.max());
        Assert.assertEquals(stats.getSum(), petAges.sum());
        Assert.assertEquals(stats.getAverage(), petAges.average(), 0.0);
        Assert.assertEquals(stats.getCount(), petAges.size());
        // Hint: Match = Satisfy
        Assert.assertTrue(petAges.allSatisfy(i -> i > 0));
        Assert.assertFalse(petAges.anySatisfy(i -> i == 0));
        Assert.assertTrue(petAges.noneSatisfy(i -> i < 0));

        // Don't forget to comment this out or delete it when you are done
    }

    @Test
    public void streamsToECRefactor1()
    {
        //find Bob Smith
        Person person =
                this.people.detectWith(Person::named, "Bob Smith");

        //get Bob Smith's pets' names
        String names =
                person.getPets()
                        .collect(Pet::getName)
                        .makeString(" & ");

        Assert.assertEquals("Dolly & Spot", names);

        // Don't forget to comment this out or delete it when you are done
    }

    @Test
    public void streamsToECRefactor2()
    {
        // Hint: Try to replace the Map<PetType, Long> with a Bag<PetType>
        MutableBag<PetType> countsStream = this.people.flatCollect(Person::getPetTypes).toBag();
        Assert.assertEquals(2L, countsStream.occurrencesOf(PetType.CAT));
        Assert.assertEquals(2L, countsStream.occurrencesOf(PetType.DOG));
        Assert.assertEquals(2L, countsStream.occurrencesOf(PetType.HAMSTER));
        Assert.assertEquals(1L, countsStream.occurrencesOf(PetType.SNAKE));
        Assert.assertEquals(1L, countsStream.occurrencesOf(PetType.TURTLE));
        Assert.assertEquals(1L, countsStream.occurrencesOf(PetType.BIRD));

        // Don't forget to comment this out or delete it when you are done
    }

    /**
     * The purpose of this test is to determine the top 3 pet types
     */
    @Test
    public void streamsToECRefactor3()
    {
        // Hint: The result of groupingBy/counting can almost always be replaced by a Bag
        // Hint: Look for the API on Bag that might return the top 3 pet types
        MutableList<ObjectIntPair<PetType>> favoritesStream =
                this.people.flatCollect(Person::getPets)
                        .collect(Pet::getType)
                        .toBag()
                        .topOccurrences(3);
        Verify.assertSize(3, favoritesStream);
        Verify.assertContains(PrimitiveTuples.pair(PetType.CAT, 2), favoritesStream);
        Verify.assertContains(PrimitiveTuples.pair(PetType.DOG, 2), favoritesStream);
        Verify.assertContains(PrimitiveTuples.pair(PetType.HAMSTER, 2), favoritesStream);

        // Don't forget to comment this out or delete it when you are done
//        Assert.fail("Refactor to Eclipse Collections");
    }
}
