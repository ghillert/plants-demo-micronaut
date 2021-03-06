/*
 * Copyright 2020 Gunnar Hillert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hillert.micronaut.plants.dao;

import com.hillert.micronaut.plants.model.Image;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gunnar Hillert
 * @since 1.0
 */
@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class ImageRepository implements PageableRepository<Image, Long> {

	private final JdbcOperations jdbcOperations;

	public ImageRepository(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@Transactional
	public List<Long> getImageIdsForPlantId(Long plantId) {
		final String sql = "SELECT i.id as id from images i left join plants p on i.plant_id = p.id WHERE p.id = ?";
		return this.jdbcOperations.prepareStatement(sql, statement -> {
			statement.setLong(1, plantId);
			final ResultSet resultSet = statement.executeQuery();
			final List<Long> imageIds = new ArrayList<>();
			while(resultSet.next()) {
				final Long imageId = resultSet.getLong("id");
				imageIds.add(imageId);
			}
			return imageIds;
		});
	}

}
