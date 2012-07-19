package nicol

import renderer._

/**
 * This is a convenient trait that mixes in the common scene functionality
 * for you.
 */
trait BasicScene extends GameScene with SyncableScene with StandardRenderer
