// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.common.pool;

import java.util.Objects;

/**
 * {@link ResourcePool} statistics
 */
public final class ResourcePoolStats {

  /**
   * number of resource allocations
   */
  public final int allocations;

  /**
   * number of evicted resources
   */
  public final int evictions;

  /**
   * number of idle resources
   */
  public final int idle;

  /**
   * number of resources assigned to consumers
   */
  public final int inUse;

  /**
   * The idle to inUse ratio
   */
  public final float idleToInUse;

  /**
   * @param allocations number of resource allocations
   * @param evictions   number of evicted resources
   * @param idle        number of idle resources
   */
  public ResourcePoolStats(int allocations, int evictions, int idle) {
    this.allocations = allocations;
    this.evictions = evictions;
    this.idle = idle;
    this.inUse = allocations - evictions - idle;
    this.idleToInUse = (float) idle / Math.max(1, inUse);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResourcePoolStats that = (ResourcePoolStats) o;
    return allocations == that.allocations &&
        evictions == that.evictions &&
        idle == that.idle;
  }

  @Override
  public int hashCode() {
    return Objects.hash(allocations, evictions, idle);
  }

  @Override
  public String toString() {
    return String.format("ResourcePoolStats(allocations: %d, evictions: %d, idle: %d, inUse: %d, idleToInUse: %f)",
        allocations, evictions, idle, inUse, idleToInUse);
  }
}
