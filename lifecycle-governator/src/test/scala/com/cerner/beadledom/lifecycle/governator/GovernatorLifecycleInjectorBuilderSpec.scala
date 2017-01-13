package com.cerner.beadledom.lifecycle.governator

import com.google.inject.{AbstractModule, Module}
import com.netflix.governator.LifecycleManager
import com.netflix.governator.ShutdownHookModule.SystemShutdownHook
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FunSpec, MustMatchers}
import scala.collection.JavaConverters._

/**
  * Unit tests for [[GovernatorLifecycleInjectorBuilder]].
  *
  * @author John Leacox
  */
class GovernatorLifecycleInjectorBuilderSpec extends FunSpec with MustMatchers with MockitoSugar {
  describe("GovernatorLifecycleInjectorBuilder") {
    describe("#modules") {
      it("throws a NullPointerException if the modules param is null") {
        val builder = new GovernatorLifecycleInjectorBuilder

        intercept[NullPointerException] {
          builder.modules(null)
        }
      }
    }

    describe("#createInjector") {
      it("creates an injector with a binding for LifecycleManager") {
        val injector = new GovernatorLifecycleInjectorBuilder().createInjector()
        injector.getInstance(classOf[LifecycleManager]) must not be null
      }

      it("creates an injector with a binding for SystemShutdownHook") {
        val injector = new GovernatorLifecycleInjectorBuilder().createInjector()
        injector.getInstance(classOf[SystemShutdownHook]) must not be null
      }

      it("creates an injector including user specified modules") {
        val builder = new GovernatorLifecycleInjectorBuilder
        val module: Module = new AbstractModule() {
          override def configure(): Unit = {
            bind(classOf[String]).toInstance("Hello World")
          }
        }

        builder.modules(List(module).asJava)

        val injector = builder.createInjector
        injector.getInstance(classOf[String]) mustBe "Hello World"
      }
    }
  }
}
