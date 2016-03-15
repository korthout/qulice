/**
 * Copyright (c) 2011-2016, Qulice.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the Qulice.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.qulice.pmd;

import com.jcabi.log.Logger;
import com.qulice.spi.Environment;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import net.sourceforge.pmd.ReportListener;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.stat.Metric;

/**
 * Listener of PMD errors.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @author Dmitry Bashkin (dmitry.bashkin@qulice.com)
 * @version $Id$
 * @since 0.3
 */
final class PmdListener implements ReportListener {

    /**
     * Environment.
     */
    private final Environment env;

    /**
     * Violations.
     */
    private final Collection<RuleViolation> violations;

    /**
     * Public ctor.
     * @param environ Environment
     */
    PmdListener(final Environment environ) {
        this.violations = new LinkedList<RuleViolation>();
        this.env = environ;
    }

    @Override
    public void metricAdded(final Metric metric) {
        // ignore it
    }

    @Override
    public void ruleViolationAdded(final RuleViolation violation) {
        final String name = violation.getFilename().substring(
            this.env.basedir().toString().length()
        );
        if (!this.env.exclude("pmd", name)) {
            this.violations.add(violation);
            Logger.error(
                this,
                "%s[%d-%d]: %s (%s)",
                name,
                violation.getBeginLine(),
                violation.getEndLine(),
                violation.getDescription(),
                violation.getRule().getName()
            );
        }
    }

    /**
     * Get list of violations.
     * @return List of violations
     */
    public Collection<RuleViolation> getViolations() {
        return Collections.unmodifiableCollection(this.violations);
    }

}
